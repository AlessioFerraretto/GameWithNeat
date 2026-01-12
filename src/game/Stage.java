package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import neat.Genome;

public abstract class Stage {

	protected int startingX, startingY;
	protected Mario playerMario;
	protected ArrayList<Mario> arrayMario;
	protected Enemy[][] enemiesMatrix;
	protected Block[][] blocksMatrix;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<Block> blocks;
	protected int generation = 1;
	Genome fittest;

	public Stage() {
		arrayMario = new ArrayList<Mario>();
		playerMario = new Mario(this);
	}

	public void draw(Graphics g) {
		for(Block block : blocks) {
			block.draw(g);
		}

		for (Enemy e : enemies) {
			e.draw(g);
		}

		if(arrayMario.size() != 0) {
			for(Mario mario : arrayMario) {
				mario.draw(g);
			}
			g.setColor(Color.white);
			g.setFont(new Font("Verdana", Font.PLAIN, 24));
			g.drawString("Generation : " + generation, 10, 20);

		} else {
			playerMario.draw(g);
		}

		if(fittest!=null) {
			fittest.draw(g);
		}
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void tick() {
		playerMario.tick();
		for(Mario mario : arrayMario) {
			mario.tick();
			for (Enemy e : enemies) {
				e.tick();
			}	
		}	
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public ArrayList<BaseActor> getEntities() {
		ArrayList<BaseActor> r = new ArrayList<>();
		for(Block b : getBlocks()) {
			r.add(b);
		}
		for(Enemy e : getEnemies()) {
			r.add(e);
		}

		return r;

	}

	public ArrayList<Mario> getArrayMario() {
		return arrayMario;
	}

	protected void setStage(String st) {
		File f = new File(st);
		try {
			Scanner sc = new Scanner(new FileInputStream(f));
			BlockType bt = null;
			blocksMatrix = new Block[sc.nextInt()][sc.nextInt()];
			sc.nextLine();
			int i=0, j=0;
			while(sc.hasNext()) {
				String[] s = sc.nextLine().split(" ");
				for(int b=0;b<s.length;b++) {
					int r = Integer.valueOf(s[b]);
					for(int a=0;a<BlockType.values().length;a++) {
						if(BlockType.values()[a].getValue() == r) {
							bt = BlockType.values()[a];
							break;
						}
					}
					blocksMatrix[i][j] = new Block(i*Block.BLOCK_SIZE,j*Block.BLOCK_SIZE, bt, this);
					i++;
				}
				i=0;
				j++;
			}

		} catch (Exception e) {
		}

		blocks = new ArrayList<>();

		for(Block[] row : blocksMatrix) {
			for(Block el : row) {
				if(el!=null) {
					blocks.add(el);
				}
			}
		}


	}

	protected void setEnemies(String st) {
		File f = new File(st);
		try {
			Scanner sc = new Scanner(new FileInputStream(f));
			EnemyType et = null;
			enemiesMatrix = new Enemy[sc.nextInt()][sc.nextInt()];
			sc.nextLine();
			int i=0, j=0;
			while(sc.hasNext()) {
				String[] s = sc.nextLine().split(" ");
				for(int b=0;b<s.length;b++) {
					int r = Integer.valueOf(s[b]);
					for(int a=0;a<EnemyType.values().length;a++) {
						if(EnemyType.values()[a].getValue() == r) {
							et = EnemyType.values()[a];
							enemiesMatrix[i][j] = new Enemy(this, i*Block.BLOCK_SIZE, j*Block.BLOCK_SIZE, et, null);

							break;
						}
					}
					i++;
				}
				i=0;
				j++;
			}

		} catch (Exception e) {
		}

		enemies = new ArrayList<>();

		for(Enemy[] row : enemiesMatrix) {
			for(Enemy el : row) {
				if(el!=null) {
					enemies.add(el);
				}
			}
		}

		playerMario.setWin(getWin());
	}

	protected void loadMario() {
		playerMario.setX(startingX);
		playerMario.setY(startingY);

	}

	public void loadArrayMario(int N) {
		Enemy win = getWin();

		arrayMario = new ArrayList<>();
		for(int i=0;i<N;i++) {
			arrayMario.add(new Mario(this, win));
			arrayMario.get(i).setStage(this);
			arrayMario.get(i).setX(startingX);
			arrayMario.get(i).setY(startingY);
		}		

	}

	public Mario getPlayerMario() {
		return playerMario;
	}

	public ArrayList<Block> getBlocksNear(Mario mario, int k) {
		ArrayList<Block> r = new ArrayList<>();
		ArrayList<Double> distances = new ArrayList<>();
		Point m = new Point((int) mario.getRectangle().getCenterX(),(int) mario.getRectangle().getCenterY());
		for(Block block : blocks) {
			distances.add(m.distance(new Point((int) block.getRectangle().getCenterX(),(int) block.getRectangle().getCenterY())));
		}
		double distanceMax = findDistanceofKElements(distances, k);
		for(Block block : blocks) {
			double d = m.distance(new Point((int) block.getRectangle().getCenterX(),(int) block.getRectangle().getCenterY()));
			if(d<=distanceMax) {
				if(r.size()<k) {
					r.add(block);
				}
				else {
					return r;
				}
			}
		}

		return r;
	}

	public void setGeneration(int n) {
		generation = n;
	}

	static Double findDistanceofKElements(ArrayList<Double> list, int k) {
		if(k == 0) {
			return 0d;
		}
		Collections.sort(list);

		return list.get(k);
	}

	public Enemy getWin() {
		return enemies.stream().filter(e->e.getTouchType().equals(TouchType.WIN)).findFirst().get();
	}

	public void setFittest(Genome fittest) {
		this.fittest = fittest;		
	}

	public Block getBlockMarioIsIn(Mario mario) {
		Block r;
		ArrayList<Double> distances = new ArrayList<>();
		Point m = new Point((int) mario.getRectangle().getCenterX(),(int) mario.getRectangle().getCenterY());
		for(Block block : blocks) {
			distances.add(m.distance(new Point((int) block.getRectangle().getCenterX(),(int) block.getRectangle().getCenterY())));
		}
		int minBlockIndex = distances.indexOf(distances.stream().sorted().findFirst().get());

		return blocks.get(minBlockIndex);
	}

	public ArrayList<BaseActor> getThingsAround(Block b, int left, int right) {
		ArrayList<BaseActor> result  = new ArrayList<BaseActor>();

		int indexI=-1, indexJ=-1;
		for(int i=0;i<blocksMatrix.length;i++) {
			for(int j=0; j<blocksMatrix[0].length;j++) {
				if(blocksMatrix[i][j].equals(b)) {
					indexI = i;
					indexJ = j;
					break;
				}
			}
		}

		for(int i=indexI-left;i<=indexI+right;i++) {
			for(int j=indexJ-left;j<=indexJ+right;j++) {
				try {
					if(enemiesMatrix[i][j] != null) {
						result.add(enemiesMatrix[i][j]);
					} else {
						result.add(blocksMatrix[i][j]);
					}
				} catch(Exception e) {
					result.add(new Block(0,0, BlockType.AIR, this));
				}
			}
		}
		result.remove(b);

		if(result.size()!=48) {
			System.out.println(result.size());
		}
		//		for(Block block : blocks) {
		//			if(block.getX() <= b.getX() + Block.BLOCK_SIZE*distance && block.getX() >= b.getX() - Block.BLOCK_SIZE*distance) {
		//				if(block.getY() <= b.getY() + Block.BLOCK_SIZE*distance && block.getY() >= b.getY() - Block.BLOCK_SIZE*distance) {
		//					boolean enemy = false;
		//					for(Enemy e : enemies) {
		//						if(e.collidesWith(block.getRectangle()) && e.getTouchType().equals(TouchType.DIE)) {
		//							result.add(e);
		//							enemy = true;
		//							break;
		//						}
		//					}
		//					if(!enemy) {
		//						result.add(block);
		//					}
		//				}
		//			}
		//		}
		//
		//		result.remove(b);

		return result;

	}


	public ArrayList<Enemy> findNearestEnemies(Mario mario, int k) {
		ArrayList<Enemy> r = new ArrayList<>();
		ArrayList<Double> distances = new ArrayList<>();
		Point m = new Point((int) mario.getRectangle().getCenterX(),(int) mario.getRectangle().getCenterY());
		for(Enemy enemy : enemies) {
			if(enemy.getTouchType().equals(TouchType.DIE))
				distances.add(m.distance(new Point((int) enemy.getRectangle().getCenterX(),(int) enemy.getRectangle().getCenterY())));
		}
		double distanceMax = Stage.findDistanceofKElements(distances, k);
		for(Enemy enemy : enemies) {
			double d = m.distance(new Point((int) enemy.getRectangle().getCenterX(),(int) enemy.getRectangle().getCenterY()));
			if(d<=distanceMax && enemy.getTouchType().equals(TouchType.DIE)) {
				if(r.size()<k) {
					r.add(enemy);
				}
				else {
					return r;
				}
			}
		}

		return r;	
	}

	public void sostituisci(int i, int j, BaseActor selected) {
		selected.setX(i * Block.BLOCK_SIZE);
		selected.setY(j * Block.BLOCK_SIZE);
		selected.setStage(this);
		if(selected instanceof Block) {
			blocksMatrix[i][j] = (Block) selected;
			blocks = new ArrayList<>();
			for(Block[] row : blocksMatrix) {
				for(Block el : row) {
					if(el!=null) {
						blocks.add(el);
					}
				}
			}
			
		} else {
			enemiesMatrix[i][j] = (Enemy) selected;
			enemies = new ArrayList<>();
			for(Enemy[] row : enemiesMatrix) {
				for(Enemy el : row) {
					if(el!=null) {
						enemies.add(el);
					}
				}
			}
		}
	}
}
