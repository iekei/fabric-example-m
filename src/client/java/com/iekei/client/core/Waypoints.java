package com.iekei.client.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/** Shared detection store: finders write, HUD/nav modules read. */
public final class Waypoints {
	public record Point(String kind, BlockPos pos, long time) {
	}

	private static final List<Point> POINTS = new ArrayList<>();

	private Waypoints() {
	}

	public static void add(String kind, BlockPos pos) {
		for (Point p : POINTS) {
			if (p.kind().equals(kind) && p.pos().distManhattan(pos) <= 2) {
				return;
			}
		}

		POINTS.add(new Point(kind, pos.immutable(), System.currentTimeMillis()));

		while (POINTS.size() > 3000) {
			POINTS.remove(0);
		}
	}

	public static List<Point> all() {
		return POINTS;
	}

	public static List<Point> ofKind(String kind) {
		return POINTS.stream().filter(p -> p.kind().equals(kind)).toList();
	}

	public static Point nearest(String kind, Vec3 from) {
		Point best = null;
		double bestDistance = Double.MAX_VALUE;

		for (Point p : POINTS) {
			if (!p.kind().equals(kind)) {
				continue;
			}

			double d = p.pos().getCenter().distanceToSqr(from);

			if (d < bestDistance) {
				bestDistance = d;
				best = p;
			}
		}

		return best;
	}

	public static void clear() {
		POINTS.clear();
	}
}