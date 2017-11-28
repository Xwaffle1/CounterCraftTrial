package com.ferullogaming.countercraft.util;

import net.minecraft.world.World;

/**
 * Represents a 3-dimensional position in a world
 */
public class Location implements Cloneable {
	private World world;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	/**
	 * Constructs a new Location with the given coordinates
	 *
	 * @param world
	 *            The world in which this location resides
	 * @param x
	 *            The x-coordinate of this new location
	 * @param y
	 *            The y-coordinate of this new location
	 * @param z
	 *            The z-coordinate of this new location
	 */
	public Location(final World world, final double x, final double y, final double z) {
		this(world, x, y, z, 0, 0);
	}

	/**
	 * Constructs a new Location with the given coordinates and direction
	 *
	 * @param world
	 *            The world in which this location resides
	 * @param x
	 *            The x-coordinate of this new location
	 * @param y
	 *            The y-coordinate of this new location
	 * @param z
	 *            The z-coordinate of this new location
	 * @param yaw
	 *            The absolute rotation on the x-plane, in degrees
	 * @param pitch
	 *            The absolute rotation on the y-plane, in degrees
	 */
	public Location(final World world, final double x, final double y, final double z, final float yaw, final float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	/**
	 * Sets the world that this location resides in
	 *
	 * @param world
	 *            New world that this location resides in
	 */
	public void setWorld(World world) {
		this.world = world;
	}

	/**
	 * Gets the world that this location resides in
	 *
	 * @return World that contains this location
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Sets the x-coordinate of this location
	 *
	 * @param x
	 *            X-coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the x-coordinate of this location
	 *
	 * @return x-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the y-coordinate of this location
	 *
	 * @param y
	 *            y-coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the y-coordinate of this location
	 *
	 * @return y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the z-coordinate of this location
	 *
	 * @param z
	 *            z-coordinate
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Gets the z-coordinate of this location
	 *
	 * @return z-coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Sets the yaw of this location, measured in degrees.
	 * <ul>
	 * <li>A yaw of 0 or 360 represents the positive z direction.
	 * <li>A yaw of 180 represents the negative z direction.
	 * <li>A yaw of 90 represents the negative x direction.
	 * <li>A yaw of 270 represents the positive x direction.
	 * </ul>
	 * Increasing yaw values are the equivalent of turning to your right-facing,
	 * increasing the scale of the next respective axis, and decreasing the scale of
	 * the previous axis.
	 *
	 * @param yaw
	 *            new rotation's yaw
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	/**
	 * Gets the yaw of this location, measured in degrees.
	 * <ul>
	 * <li>A yaw of 0 or 360 represents the positive z direction.
	 * <li>A yaw of 180 represents the negative z direction.
	 * <li>A yaw of 90 represents the negative x direction.
	 * <li>A yaw of 270 represents the positive x direction.
	 * </ul>
	 * Increasing yaw values are the equivalent of turning to your right-facing,
	 * increasing the scale of the next respective axis, and decreasing the scale of
	 * the previous axis.
	 *
	 * @return the rotation's yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * Sets the pitch of this location, measured in degrees.
	 * <ul>
	 * <li>A pitch of 0 represents level forward facing.
	 * <li>A pitch of 90 represents downward facing, or negative y direction.
	 * <li>A pitch of -90 represents upward facing, or positive y direction.
	 * <ul>
	 * Increasing pitch values the equivalent of looking down.
	 *
	 * @param pitch
	 *            new incline's pitch
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	/**
	 * Gets the pitch of this location, measured in degrees.
	 * <ul>
	 * <li>A pitch of 0 represents level forward facing.
	 * <li>A pitch of 90 represents downward facing, or negative y direction.
	 * <li>A pitch of -90 represents upward facing, or positive y direction.
	 * <ul>
	 * Increasing pitch values the equivalent of looking down.
	 *
	 * @return the incline's pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Adds the location by another.
	 *
	 * @see Vector
	 * @param vec
	 *            The other location
	 * @return the same location
	 * @throws IllegalArgumentException
	 *             for differing worlds
	 */
	public Location add(Location vec) {
		if (vec == null || vec.getWorld() != getWorld()) {
			throw new IllegalArgumentException("Cannot add Locations of differing worlds");
		}

		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	/**
	 * Adds the location by another. Not world-aware.
	 *
	 * @see Vector
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @return the same location
	 */
	public Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	/**
	 * Subtracts the location by another.
	 *
	 * @see Vector
	 * @param vec
	 *            The other location
	 * @return the same location
	 * @throws IllegalArgumentException
	 *             for differing worlds
	 */
	public Location subtract(Location vec) {
		if (vec == null || vec.getWorld() != getWorld()) {
			throw new IllegalArgumentException("Cannot add Locations of differing worlds");
		}

		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	/**
	 * Subtracts the location by another. Not world-aware and orientation
	 * independent.
	 *
	 * @see Vector
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @return the same location
	 */
	public Location subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	/**
	 * Performs scalar multiplication, multiplying all components with a scalar. Not
	 * world-aware.
	 *
	 * @param m
	 *            The factor
	 * @see Vector
	 * @return the same location
	 */
	public Location multiply(double m) {
		x *= m;
		y *= m;
		z *= m;
		return this;
	}

	/**
	 * Zero this location's components. Not world-aware.
	 *
	 * @see Vector
	 * @return the same location
	 */
	public Location zero() {
		x = 0;
		y = 0;
		z = 0;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Location other = (Location) obj;

		if (this.world != other.world && (this.world == null || !this.world.equals(other.world))) {
			return false;
		}
		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
			return false;
		}
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
			return false;
		}
		if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
			return false;
		}
		if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
			return false;
		}
		if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;

		hash = 19 * hash + (this.world != null ? this.world.hashCode() : 0);
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
		hash = 19 * hash + Float.floatToIntBits(this.pitch);
		hash = 19 * hash + Float.floatToIntBits(this.yaw);
		return hash;
	}

	@Override
	public String toString() {
		return "Location{" + "world=" + world + ",x=" + x + ",y=" + y + ",z=" + z + ",pitch=" + pitch + ",yaw=" + yaw + '}';
	}

	@Override
	public Location clone() {
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}