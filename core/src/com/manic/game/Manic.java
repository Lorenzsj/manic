package com.manic.game;

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.manic.game.exceptions.InvalidXMLException;
import com.manic.game.helper.FileStuff;
import com.manic.game.resource_management.AnimationResourceManager;
import com.manic.game.resource_management.Moves;
import com.manic.game.states.GameStateManager;
import com.manic.game.xml.AnimationResourceManagerDataParser;

/**
 * 
 * @brief This is the main core class
 * 
 * This is called by the respective launchers for PC, Android, and iOS.
 * This basically initializes a bunch of stuff and continually updates/renders the states
 * 
 * @author Dylan Robinson, Gabe Tucker, Stephen Lorenz
 * 
 * @contact gst06@roadrunner.com
 *
 */
public class Manic implements ApplicationListener
{
	
	Texture img;
	public static final String TITLE = "Manic";
	public static final int V_WIDTH = 320;
	public static final int V_HEIGHT = 240;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	private float accum;
	public static SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;
	private GameStateManager gsm;
	public Font font;
	
	
	public static AnimationResourceManager res_animations;
	public static Moves res_moves;
	
	
	//Makeshift sound manager
	public static HashMap < String , Sound > res_sounds;
	
	
	public static boolean changeStateLock = false;
	
	//This counter is used to give unique identification numbers
	public static int counter = 0;
	
	
	
	public static SpriteBatch getSpriteBatch()
	{
		return batch;
	}
	
	public OrthographicCamera getCamera()
	{
		return camera;
	}
	
	public OrthographicCamera getHUDCamera()
	{
		return hudCamera;
	}
	
	public void pause() {}
	
	
	
	@Override
	public void create ()
	{
		try {
			
			//Load animations
			res_animations = new AnimationResourceManager();
			
			
			
			AnimationResourceManagerDataParser p 
						= new AnimationResourceManagerDataParser(
								res_animations);
			
			String xml = FileStuff.fileToString(
						"../resources/sprites.xml");
			
			p.parse(xml); ///This fills the res_animations
			
			
			//Load sounds
			res_sounds = new HashMap < String , Sound >();
			loadSounds();
			
			
			//Load moves
			res_moves = new Moves();
			res_moves.load();
			
			
			Gdx.input.setInputProcessor(new InputProcessor());
			
			batch = new SpriteBatch();
			camera = new OrthographicCamera();
			camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
			hudCamera = new OrthographicCamera();
			hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);
			
			gsm = new GameStateManager(this);
			
		}	
		catch (InvalidXMLException e) {
			
			System.out.println( e.getMessage() );
			
		} catch (IOException e) {
			
			System.out.println( e.getMessage() );

		}

		
		
	}

	private void loadSounds() {
		
		res_sounds.put ( "attack" , Gdx.audio.newSound(Gdx.files.internal("../resources/sounds/attack.wav")));
		res_sounds.put ( "hit" , Gdx.audio.newSound(Gdx.files.internal("../resources/sounds/hit.wav")));
		res_sounds.put ( "new-round" , Gdx.audio.newSound(Gdx.files.internal("../resources/sounds/new-round.wav")));
		res_sounds.put ( "victory" , Gdx.audio.newSound(Gdx.files.internal("../resources/sounds/victory.wav")));
		
	}

	@Override
	public void render ()
	{
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;

			
			gsm.update(STEP);
			if (!changeStateLock) 
				gsm.render();
			
			InputHandler.update();
			
			
			changeStateLock = false;
			
		}
	}
	
	public void resume()
	{
		
	}
	
	public void resize(int height, int width)
	{
		
	}
	
	public void dispose()
	{
		
	}
}
