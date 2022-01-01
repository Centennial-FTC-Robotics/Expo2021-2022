package expo.logger;

public class Item {
	private String caption;
	private String value;
	private int priority;
	private boolean retained = false;
	
	public Item() {
		this.caption = "";
		this.value = "";
		this.priority = Integer.MAX_VALUE;
	}
	public Item (String caption, String value) {
		this.caption = caption;
		this.value = value;
		this.priority = Integer.MAX_VALUE;
	}
	
	public Item (String caption, Object value, int priority) {
		this.caption = caption;
		this.value = value.toString();
		this.priority = priority;
	}
	
	public Item (String caption, Object value) {
		this.caption = caption;
		this.value = value.toString();
		this.priority = Integer.MAX_VALUE;
	}
	

	
	public String getCaption () {
		return caption;
	}
	
	public String getValue () {
		return value;
	}
	
	public int getPriority () {
		return priority;
	}
	
	public boolean isRetained () {
		return retained;
	}
	
	public Item setRetained (boolean retained) {
		this.retained = retained;
		return this;
	}
	
	public static class Line extends Item {
		
		public Line (String caption) {
			super(caption, "");
		}
		
		public Line (String caption, int priority) {
			super(caption, "", priority);
		}
	}
}
