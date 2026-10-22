import sys
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd

folder = Path(sys.argv[1] if len(sys.argv) > 1 else "results")
df = pd.read_csv(folder / "summary.csv")
algos = list(df["algorithm"].unique())

plt.style.use("ggplot")
plt.rcParams.update({"font.size": 11, "figure.dpi": 120})


def line_plot(metric_col: str, logscale: bool, filename: str, title: str, ylabel: str, plot_std_band: bool = False):
    if metric_col not in df.columns:
        print(f"Skipping {filename}: Column '{metric_col}' not found.")
        return

    fig, ax = plt.subplots(figsize=(9, 6))
    for algo in algos:
        d = df[df["algorithm"] == algo].sort_values("n")
        ax.plot(d["n"], d[metric_col], marker="o", label=algo)
        
        if plot_std_band and not logscale and "stddev_ms" in df.columns:
            ax.fill_between(d["n"], (d[metric_col] - d["stddev_ms"]).clip(lower=0),
                            d[metric_col] + d["stddev_ms"], alpha=0.15)
            
    if logscale:
        ax.set_xscale("log")
        ax.set_yscale("log")
    ax.set_xlabel("Array size n")
    ax.set_ylabel(ylabel)
    ax.set_title(title, fontweight="bold")
    ax.legend()
    fig.tight_layout()
    fig.savefig(folder / filename)
    plt.close(fig)
    print("wrote", folder / filename)


def ratio_plot(metric_col: str, filename: str, title: str):
    if metric_col not in df.columns or "Sort" not in algos:
        return
        
    base = df[df["algorithm"] == "Sort"].set_index("n")[metric_col]
    fig, ax = plt.subplots(figsize=(9, 6))
    for algo in algos:
        if algo == "Sort":
            continue
        d = df[df["algorithm"] == algo].set_index("n")[metric_col].sort_index()
        ax.plot(d.index, d / base.loc[d.index], marker="o", label=f"{algo} / Sort")
        
    ax.axhline(1.0, color="gray", linestyle="--")
    ax.set_xscale("log")
    ax.set_xlabel("Array size n")
    ax.set_ylabel(f"Time relative to Sort ({metric_col})")
    ax.set_title(title, fontweight="bold")
    ax.legend()
    fig.tight_layout()
    fig.savefig(folder / filename)
    plt.close(fig)
    print("wrote", folder / filename)


line_plot("mean_ms", False, "plot_mean_linear.png", "Median selection: mean running time", "Mean time (ms)", plot_std_band=True)
ratio_plot("mean_ms", "plot_mean_ratio.png", "Speed relative to sorting baseline (Mean)")

line_plot("median_ms", False, "plot_median_linear.png", "Median selection: median running time", "Median time (ms)", plot_std_band=True)
ratio_plot("median_ms", "plot_median_ratio.png", "Speed relative to sorting baseline (Median)")

line_plot("stddev_ms", False, "plot_stddev_linear.png", "Algorithm Stability (Standard Deviation)", "Standard Deviation (ms)", plot_std_band=False)