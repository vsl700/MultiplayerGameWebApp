package com.vsl700.pong;

public class Location {
	private float x;
	private float y;
	
	public Location(Location anotherLoc) {
		this(anotherLoc.x, anotherLoc.y);
	}
	
	public Location(float x, float y) {
		set(x, y);
	}
	
	public Location set(float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Location set(Location location) {
		x = location.x;
		y = location.y;
		
		return this;
	}
	
	/**
	 * 
	 * @return a new copy of this Location
	 */
	public Location copy() {
		return new Location(this);
	}
	
	public Location add(Location location) {
		x += location.x;
		y += location.y;
		
		return this;
	}
	
	public Location sub(Location location) {
		x -= location.x;
		y -= location.y;
		
		return this;
	}
	
	public Location sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		
		return this;
	}
	
	public Location mul(float multiplier) {
		x *= multiplier;
		y *= multiplier;
		
		return this;
	}
	
	public Location mul(float x, float y) {
		this.x *= x;
		this.y *= y;
		
		return this;
	}
	
	public Location normalize() {
		mul(1 / len());
		
		return this;
	}
	
	public float len() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "x:" + x + " y:" + y;
	}
}
