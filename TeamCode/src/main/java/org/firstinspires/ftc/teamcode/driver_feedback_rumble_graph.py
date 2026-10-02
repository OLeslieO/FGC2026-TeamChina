#!/usr/bin/env python3
"""
Generate a velocity graph for the "Driver Feedback: Gamepad Rumble" notebook section.

By default this script creates an SVG graph showing the shooter velocity rising
toward its target while the rumble threshold marks when driver feedback begins.
It can also graph real telemetry from a CSV file with these columns:

    time,actual_velocity,target_velocity

Usage:
    python3 driver_feedback_rumble_graph.py
    python3 driver_feedback_rumble_graph.py --csv shooter_log.csv
    python3 driver_feedback_rumble_graph.py --output gamepad-rumble-graph.svg
"""

from __future__ import annotations

import argparse
import csv
import html
import math
from pathlib import Path


DEFAULT_OUTPUT = "gamepad-rumble-velocity-graph.svg"
DEFAULT_TARGET_VELOCITY = 1800.0
DEFAULT_RUMBLE_THRESHOLD = 1400.0


def example_data() -> tuple[list[float], list[float], list[float]]:
    """Create an example response similar to a flywheel shooter in TeleOp."""
    times = [index * 0.1 for index in range(81)]
    actual_velocity = []

    for time_s in times:
        spin_up = DEFAULT_TARGET_VELOCITY * (1.0 - math.exp(-time_s / 0.85))
        overshoot = 155.0 * math.exp(-((time_s - 1.7) / 0.45) ** 2)
        settling_wave = 85.0 * math.sin((time_s - 1.1) * 5.4) * math.exp(-max(0.0, time_s - 1.1) / 1.7)
        shot_dip = 245.0 * math.exp(-((time_s - 4.7) / 0.23) ** 2)
        recovery_bump = 55.0 * math.exp(-((time_s - 5.25) / 0.42) ** 2)
        sensor_noise = 14.0 * math.sin(time_s * 13.0) + 7.0 * math.sin(time_s * 27.0)

        velocity = spin_up + overshoot + settling_wave - shot_dip + recovery_bump + sensor_noise
        velocity = max(0.0, min(velocity, DEFAULT_TARGET_VELOCITY + 120.0))
        actual_velocity.append(velocity)

    target_velocity = [DEFAULT_TARGET_VELOCITY for _ in times]
    return times, actual_velocity, target_velocity


def csv_data(csv_path: Path) -> tuple[list[float], list[float], list[float]]:
    times: list[float] = []
    actual_velocity: list[float] = []
    target_velocity: list[float] = []

    with csv_path.open(newline="") as csv_file:
        reader = csv.DictReader(csv_file)
        required_columns = {"time", "actual_velocity", "target_velocity"}
        missing_columns = required_columns - set(reader.fieldnames or [])
        if missing_columns:
            missing = ", ".join(sorted(missing_columns))
            raise ValueError(f"CSV is missing required column(s): {missing}")

        for row in reader:
            times.append(float(row["time"]))
            actual_velocity.append(float(row["actual_velocity"]))
            target_velocity.append(float(row["target_velocity"]))

    if not times:
        raise ValueError("CSV did not contain any data rows.")

    return times, actual_velocity, target_velocity


def draw_graph(
    times: list[float],
    actual_velocity: list[float],
    target_velocity: list[float],
    output_path: Path,
    rumble_threshold: float,
) -> None:
    width = 1000
    height = 620
    margin_left = 86
    margin_right = 40
    margin_top = 78
    margin_bottom = 82
    plot_width = width - margin_left - margin_right
    plot_height = height - margin_top - margin_bottom

    x_min = min(times)
    x_max = max(times)
    y_min = max(0.0, min(actual_velocity + target_velocity + [rumble_threshold]) - 120.0)
    y_max = max(actual_velocity + target_velocity + [rumble_threshold]) + 170.0

    def x_scale(value: float) -> float:
        if x_max == x_min:
            return margin_left + plot_width / 2.0
        return margin_left + (value - x_min) / (x_max - x_min) * plot_width

    def y_scale(value: float) -> float:
        if y_max == y_min:
            return margin_top + plot_height / 2.0
        return margin_top + (y_max - value) / (y_max - y_min) * plot_height

    def polyline(values: list[float]) -> str:
        points = [f"{x_scale(time_s):.1f},{y_scale(value):.1f}" for time_s, value in zip(times, values)]
        return " ".join(points)

    grid_lines: list[str] = []
    x_ticks = 6
    for index in range(x_ticks + 1):
        value = x_min + (x_max - x_min) * index / x_ticks
        x = x_scale(value)
        grid_lines.append(f'<line x1="{x:.1f}" y1="{margin_top}" x2="{x:.1f}" y2="{height - margin_bottom}" class="grid" />')
        grid_lines.append(f'<text x="{x:.1f}" y="{height - 42}" class="tick" text-anchor="middle">{value:.1f}</text>')

    y_ticks = 5
    for index in range(y_ticks + 1):
        value = y_min + (y_max - y_min) * index / y_ticks
        y = y_scale(value)
        grid_lines.append(f'<line x1="{margin_left}" y1="{y:.1f}" x2="{width - margin_right}" y2="{y:.1f}" class="grid" />')
        grid_lines.append(f'<text x="{margin_left - 14}" y="{y + 5:.1f}" class="tick" text-anchor="end">{value:.0f}</text>')

    rumble_y = y_scale(rumble_threshold)

    title = "Driver Feedback: Gamepad Rumble"
    svg = f"""<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}" role="img" aria-labelledby="title desc">
    <title id="title">{html.escape(title)}</title>
    <desc id="desc">Line graph comparing actual shooter velocity against target velocity, with a horizontal threshold showing where gamepad rumble begins.</desc>
    <style>
        text {{ font-family: Arial, Helvetica, sans-serif; fill: #18181b; }}
        .title {{ font-size: 28px; font-weight: 700; }}
        .axis-label {{ font-size: 15px; font-weight: 600; fill: #3f3f46; }}
        .tick {{ font-size: 13px; fill: #52525b; }}
        .grid {{ stroke: #d4d4d8; stroke-width: 1; opacity: 0.85; }}
        .axis {{ stroke: #27272a; stroke-width: 2; }}
        .actual {{ fill: none; stroke: #2563eb; stroke-width: 4; stroke-linecap: round; stroke-linejoin: round; }}
        .target {{ fill: none; stroke: #dc2626; stroke-width: 3.5; stroke-dasharray: 12 9; stroke-linecap: round; stroke-linejoin: round; }}
        .rumble-threshold {{ fill: none; stroke: #16a34a; stroke-width: 3; stroke-dasharray: 10 7; }}
        .annotation {{ font-size: 16px; font-weight: 700; fill: #166534; }}
        .annotation-small {{ font-size: 13px; fill: #166534; }}
        .legend-text {{ font-size: 14px; fill: #27272a; }}
    </style>

    <rect x="0" y="0" width="{width}" height="{height}" fill="#ffffff" />
    <text x="{width / 2:.1f}" y="42" class="title" text-anchor="middle">{html.escape(title)}</text>

    {"".join(grid_lines)}

    <line x1="{margin_left}" y1="{height - margin_bottom}" x2="{width - margin_right}" y2="{height - margin_bottom}" class="axis" />
    <line x1="{margin_left}" y1="{margin_top}" x2="{margin_left}" y2="{height - margin_bottom}" class="axis" />

    <polyline points="{polyline(actual_velocity)}" class="actual" />
    <polyline points="{polyline(target_velocity)}" class="target" />
    <line x1="{margin_left}" y1="{rumble_y:.1f}" x2="{width - margin_right}" y2="{rumble_y:.1f}" class="rumble-threshold" />
    <text x="{width - margin_right - 12}" y="{rumble_y - 12:.1f}" class="annotation" text-anchor="end">Rumble starts at {rumble_threshold:.0f} ticks/s</text>

    <text x="{width / 2:.1f}" y="{height - 14}" class="axis-label" text-anchor="middle">Time (seconds)</text>
    <text x="22" y="{height / 2:.1f}" class="axis-label" transform="rotate(-90 22 {height / 2:.1f})" text-anchor="middle">Shooter velocity (ticks/second)</text>

    <g transform="translate({width - 292}, {height - 154})">
        <rect x="-18" y="-20" width="274" height="104" fill="#ffffff" stroke="#e4e4e7" />
        <line x1="0" y1="0" x2="38" y2="0" class="actual" />
        <text x="50" y="5" class="legend-text">Actual velocity</text>
        <line x1="0" y1="30" x2="38" y2="30" class="target" />
        <text x="50" y="35" class="legend-text">Target velocity</text>
        <line x1="0" y1="62" x2="38" y2="62" class="rumble-threshold" />
        <text x="50" y="66" class="legend-text">Rumble threshold</text>
    </g>
</svg>
"""

    output_path.write_text(svg, encoding="utf-8")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Create a shooter velocity graph explaining gamepad rumble feedback."
    )
    parser.add_argument("--csv", type=Path, help="Optional CSV with time, actual_velocity, target_velocity columns.")
    parser.add_argument("--output", type=Path, default=Path(DEFAULT_OUTPUT), help="Output image path.")
    parser.add_argument(
        "--rumble-threshold",
        type=float,
        default=DEFAULT_RUMBLE_THRESHOLD,
        help="Velocity where gamepad rumble starts.",
    )
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    if args.csv:
        times, actual_velocity, target_velocity = csv_data(args.csv)
    else:
        times, actual_velocity, target_velocity = example_data()

    draw_graph(times, actual_velocity, target_velocity, args.output, args.rumble_threshold)
    print(f"Saved graph to {args.output}")


if __name__ == "__main__":
    main()
