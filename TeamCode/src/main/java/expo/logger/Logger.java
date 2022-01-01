package expo.logger;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import expo.Robot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//singleton logger class
public class Logger {
	private static Logger instance;
	private static LinearOpMode opMode;
	private List<Item> items;
	
	private Logger () {
		items = new ArrayList<>();
		opMode = Robot.opMode;
	}
	
	public static void reset () {
		if (instance != null) {
			instance.items.clear();
		}
		instance = new Logger();
	}
	
	public static Logger getInstance () {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public Item addItem (Item item) {
		items.add(item);
		return item;
	}
	
	public Item addItem (String caption, Object value, int priority) {
		return addItem(new Item(caption, value, priority));
	}
	
	public Item addItem (String caption, Object value) {
		return addItem(new Item(caption, value));
	}
	
	public Item.Line addLine (Item.Line item) {
		items.add(item);
		return item;
	}
	
	public Item.Line addLine (String caption, int priority) {
		return addLine(new Item.Line(caption, priority));
	}
	
	public Item.Line addLine (String caption) {
		return addLine(new Item.Line(caption));
	}
	
	public void update () {
		FtcDashboard dashboard = FtcDashboard.getInstance();
		TelemetryPacket packet = new TelemetryPacket();
		items.stream()
				  .sorted(Comparator.comparing(Item::getPriority))
//				  .forEach(item -> opMode.telemetry.addData(item.getCaption(), item.getValue()))
				  .forEach(
							item -> {
								opMode.telemetry.addData(item.getCaption(), item.getValue());
								packet.put(item.getCaption(), item.getValue());
							}
				  );
		
		opMode.telemetry.update();
		dashboard.sendTelemetryPacket(packet);
		
		List<Item> temp = items.stream()
				  .filter(item -> !item.isRetained())
				  .collect(Collectors.toList());
		for (Item item : temp) {
			items.remove(item);
		}
		
	}
	
	
}
