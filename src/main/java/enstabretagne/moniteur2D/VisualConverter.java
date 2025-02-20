package enstabretagne.moniteur2D;

import java.util.HashMap;

import enstabretagne.engine.EntiteSimulee;
import enstabretagne.engine.SimuEngine;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public abstract class VisualConverter {
	
	protected double canvasCenterX = 0;
	protected double canvasCenterY = 0;
	
	protected double minX;
	protected double minY;
	protected double maxX;
	protected double maxY;
	
	protected double newWidth;
	protected double newHeight;

	protected double translateX;
	protected double translateY;

	protected Canvas backgroundCanvas;
	protected GraphicsContext backgroundGc;
	
	protected Canvas objectsCanvas;
	protected GraphicsContext objectsGc;
	
	protected SimuEngine engine;
	
	
	protected <T> void addVisualMapper(Class<T> c, VisualMapper<T> vm) {
		VisualMapper<T> vc = vm;
		add(c, vc);	
	}
	
	protected void updateDimensions(double potentialMinX, double potentialMinY, double potentialMaxX, double potentialMaxY){
		potentialMinX = Math.min(potentialMinX, minX);
		potentialMinY = Math.min(potentialMinY, minY);
		potentialMaxX = Math.max(potentialMaxX, maxX);
		potentialMaxY = Math.max(potentialMaxY, maxY);
		
		double translateX = potentialMinX-minX;
		double translateY = potentialMinY-minY;
		
		minX = potentialMinX;
		minY = potentialMinY;
		maxX = potentialMaxX;
		maxY = potentialMaxY;
		
		newWidth = maxX-minX;
		newHeight = maxY-minY;
        
		objectsGc.translate(-translateX, -translateY);
		objectsCanvas.setWidth(newWidth);
		objectsCanvas.setHeight(newHeight);

		backgroundGc.translate(-translateX, -translateY);
		backgroundCanvas.setWidth(newWidth);
		backgroundCanvas.setHeight(newHeight);
	}

	@FunctionalInterface
	public interface VisualMapper<T> {
		void conv(T o);
	}
	
	public VisualConverter() {
		mappers = new HashMap<Class<?>, VisualMapper<?>>();
		
		
	}
	
	protected <T> void add(Class<T> c, VisualMapper m) {
		if(!mappers.containsKey(m))
			mappers.put(c, m);
	}
	
	void updateVisualDataModel() {
		backgroundGc.clearRect(minX, minY,maxX-minX,maxY-minY);
		objectsGc.clearRect(minX, minY,maxX-minX,maxY-minY);

		for(var e:engine.mesEntitesSimulees)
		{
			if(e.getEtat() != EntiteSimulee.EtatEntite.DEAD){
				convert(e);
			};
		}
	}

	public void init(SimuEngine engine,Canvas backgroundCanvas, Canvas objectsCanvas, double maxX, double maxY) {
		this.engine = engine;
		minX =0; minY = 0;
		this.maxX = maxX;
		this.maxY = maxX;
		
		this.backgroundCanvas = backgroundCanvas;
		backgroundGc = backgroundCanvas.getGraphicsContext2D();


        this.objectsCanvas = objectsCanvas;
		objectsGc = objectsCanvas.getGraphicsContext2D();
		
		canvasCenterX = maxX/2;
		canvasCenterY = maxY/2;
	}
	
	HashMap<Class<?>,VisualMapper<?>> mappers;
	
	public <T> void convert( T o) {
		Class<T> c = (Class<T>) o.getClass();
		if(mappers.containsKey(c)) {
			VisualMapper m = mappers.get(c);
			m.conv(o);
		}
		
	}
	
	protected enum Layers {Objects,BackGround};

	protected GraphicsContext getGc(Layers target) {
		GraphicsContext gc = null;
		if(target == Layers.Objects) {
			gc = objectsGc;
		}
		else {
			gc = backgroundGc;
		}
		return gc;
	}
	
	public void drawBackGround() {
		backgroundGc.setFill(Color.LIGHTGRAY);
        backgroundGc.fillRect(0, 0, maxX-minX, maxY-minY);
	}
	
	public void writeText(Layers target,double x,double y,Color c, String text) {
		var gc = getGc(target);
		gc.setFill(c);
		gc.setStroke(c);
	    gc.setLineWidth(2);
	    gc.setFont(new Font("Arial", 20));
	    
	    if(target == Layers.BackGround)
	    	y = y+20;
	    gc.fillText(text, x,y);
	    
	}
	public void drawCircle(boolean plain,Layers target,double center_x,double center_y,double d, Color c,String text) {
		var gc = getGc(target);
		
		double topleftx = center_x+canvasCenterX-d/2;
		double toplefty = center_y+canvasCenterY-d/2;
		
		
		updateDimensions(topleftx,toplefty,topleftx+d,toplefty+d);
		gc.setFill(c);
		gc.setStroke(c);
		
		if(plain) gc.fillOval(topleftx,toplefty , d, d);
		else gc.strokeOval(topleftx,toplefty , d, d);
		
		writeText(target,center_x+canvasCenterX,center_y+canvasCenterY,Color.BLACK,text);
	}

	
}
