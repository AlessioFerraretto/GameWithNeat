package game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import neat.Genome;
import neat.Input;
import neat.Neat;
import neat.ScoreInterface;

public class GameThread extends Thread {

	private ArrayList<Mario> arrayMario;
	private Mario playerMario;
	double maxFramesForGeneration = 150;
	int numberOfFrames = 0;
	int N = 300, AI_VISION_BLOCKS = 25, AI_VISION_ENEMIES = 2, AI_VISION_RIGHT=3, AI_VISION_LEFT=3;
	float SOGLIA = 0.5f;
	//save
	//better inputs
	private Neat neat;
	private Stage stage;
	private Enemy win;
	private Panel panel;
	private Set<Integer> inputs;
	private GameState gameState = GameState.PLAYING;

	static int FPS = 480;
	long lastFps = 0;

	private boolean ai = true;

	public GameThread(Panel p) {
		super();
		this.panel = p;
		this.inputs =  p.getInputs();

		loadStage();
		playerMario = stage.getPlayerMario();
		setAI();
	}

	private void setAI() {
		if(ai) {
			/*input:
			5x5 blocks around - itself

			output r l u*/
			arrayMario = stage.getArrayMario();

			neat = Neat.load();
			if(neat == null) {
				System.out.println("new neat run");
				neat = new Neat(
						2 + AI_VISION_BLOCKS*Block.getDataSize() + AI_VISION_ENEMIES*Enemy.getDataSize(),
						//						(int) Math.pow(AI_VISION_RIGHT+AI_VISION_LEFT+1,2)-1, //inputs
						3, //outputs
						N //n of neural networks
						);
			}


			stage.setGeneration(neat.getGeneration());

		}
	}

	public void run() {
		while(true) {
			if(lastFps > System.currentTimeMillis() - 1000/FPS ) {
				continue;
			}
			lastFps = System.currentTimeMillis();

			if(ai) {
				numberOfFrames++;
				if(numberOfFrames > maxFramesForGeneration) {

					neat.score(new ScoreInterface() {

						@Override
						public float evaluateGenome(Genome g, int i) {
							float result = 0;
							if(arrayMario.get(i).getGameState().equals(GameState.WIN)) {
								System.out.println("WIN");
								result = 10000;
							}
							else if(arrayMario.get(i).getGameState().equals(GameState.LOSE)) {
								result = 0;
							} 
							else {
								result = 10000 - arrayMario.get(i).getMinDistanceFromWin();
							}

							return result;
						}
					});
					stage.loadArrayMario(N);
					arrayMario = stage.getArrayMario();
					numberOfFrames = 0;
					maxFramesForGeneration += 1;
					stage.setGeneration(neat.getGeneration());

					if(neat.getGeneration() % 100 == 0) {
						neat.save();
					}
				}

				ArrayList<ArrayList<Input>> in = new ArrayList<ArrayList<Input>>();
				for(Mario mario : arrayMario) {
					for(Enemy e : stage.getEnemies()) {
						if(mario.collidesWith(e)) {
							switch(e.getTouchType()) {
							case DIE:
								mario.setState(GameState.LOSE);
								break;
							case WIN:
								mario.setState(GameState.WIN);
								break;
							}
						}
						mario.idle();
					}

					in.add(getAiInputData2(mario));
				}

				try {
					ArrayList<Float[]> outputs = neat.evaluate(in);
					for(int i=0;i<arrayMario.size();i++) {
						ArrayList<Integer> choices = getChoice(outputs.get(i));

						if(arrayMario.get(i).getGameState().equals(GameState.PLAYING)) {
							for(int c : choices) {
								switch(c) {
								case 0: //LEFT
									arrayMario.get(i).moveLeft();
									break;
								case 1: //RIGHT
									arrayMario.get(i).moveRight();
									break;
								case 2: //UP
									arrayMario.get(i).jump();
									break;
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				for(Enemy e : stage.getEnemies()) {
					if(playerMario.collidesWith(e)) {
						switch(e.getTouchType()) {
						case DIE:
							gameState = GameState.LOSE;
							break;
						case WIN:
							gameState = GameState.WIN;
							break;
						}
					}
					playerMario.idle();
				}


				synchronized (inputs) {
					for(Integer e : inputs) {
						if(gameState.equals(GameState.LOSE)) {
							lose();
						}
						if(gameState.equals(GameState.WIN)) {
							win();
						}

						switch(e) {
						case 87: //'w'
							playerMario.jump();
							break;
						case 65: //'a'
							playerMario.moveLeft();
							break;
						case 83: //'s'
							break;
						case 68: //'d'
							playerMario.moveRight();
							break;
						}
					}
				}

			}

			stage.setFittest(neat.getFittest());

			stage.tick();
			panel.refresh();

		}

	}

	private ArrayList<Integer> getChoice2(Float[] outputs) {
		ArrayList<Integer> result = new ArrayList<>();
		Float max = -1f;
		int indexMax=-1;
		for (int j=0;j<3;j++) {
			if(outputs[j] > max) {
				max = outputs[j];
				indexMax = j;
			}
		}
		result.add(indexMax);

		return result;
	}

	private ArrayList<Integer> getChoice(Float[] outputs) {
		ArrayList<Integer> result = new ArrayList<>();
		Float max = -1f;
		for (int j=0;j<3;j++) {
			if(outputs[j] > SOGLIA) {
				result.add(j);
			}
		}
		return result;
	}

	private ArrayList<Input> getAiInputData2(Mario mario) {
		ArrayList<Input> inputData = new ArrayList<>();
		inputData.add(new Input(mario.getX(), Mario.MIN_XY_VALUE, Mario.MAX_XY_VALUE));
		inputData.add(new Input(mario.getY(), Mario.MIN_XY_VALUE, Mario.MAX_XY_VALUE));

		stage.findNearestEnemies(mario, AI_VISION_ENEMIES).forEach(e->Collections.addAll(inputData, e.getData(mario)));

		stage.getBlocksNear(mario, AI_VISION_BLOCKS).forEach(b->Collections.addAll(inputData, b.getData(mario)));
		return inputData;
	}

	private ArrayList<Input> getAiInputData(Mario mario) {
		ArrayList<Input> inputData = new ArrayList<>();
		Block b = stage.getBlockMarioIsIn(mario);
		ArrayList<BaseActor> things = stage.getThingsAround(b, AI_VISION_RIGHT, AI_VISION_LEFT);
		for(BaseActor t : things) {
			inputData.add(new Input(t.getTouchType()));
		}
		return inputData;
	}

	private void win() {
		lose();		
	}

	private void lose() {
		gameState = GameState.PLAYING;
		loadStage();		
	}

	public GameState getGameState() {
		return gameState;
	}

	private void loadStage() {
		stage = new StageOne(ai ? N : 0);
		arrayMario = stage.getArrayMario();
		playerMario = stage.getPlayerMario();
		win = stage.getWin();
	}

	public Stage getStage() {
		return stage;
	}

}
