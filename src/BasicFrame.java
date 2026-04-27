import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

class StoneDraw implements MouseListener {
	JPanel contentPane = null;
	final int STONE_SIZE = 26;
	Color StoneColor = Color.BLACK; // computer white

	int scoreB = 0;
	int scoreW = 0;

	int[][] OmockBoard = new int[15][15];

	public StoneDraw(JPanel c) {
		// TODO Auto-generated constructor stub
		super();
		contentPane = c;
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++)
				OmockBoard[i][j] = 0;
		
		OmockBoard[7][7] = 1;

	}

	


	class P{
		public int i = -1;
		public int j = -1;
		int MAX = Integer.MIN_VALUE;
		int MIN = Integer.MAX_VALUE;
		public P(int _i ,int _j) {
			i = _i; j = _j;
		}
		public P() {
		}
	}
	
	public int scoreVarEstimation(int x, int y, int c) {
		  int score = 0;

		    // 가로 방향 체크
		    int count = 1; // 현재 위치의 돌을 포함하여 1로 초기화
		    int i = x + 1;
		    while (i < 15 && OmockBoard[i][y] == c) {
		        count++;
		        i++;
		    }
		    i = x - 1;
		    while (i >= 0 && OmockBoard[i][y] == c) {
		        count++;
		        i--;
		    }
		    score += getScore(count, c);

		    // 세로 방향 체크
		    count = 1;
		    int j = y + 1;
		    while (j < 15 && OmockBoard[x][j] == c) {
		        count++;
		        j++;
		    }
		    j = y - 1;
		    while (j >= 0 && OmockBoard[x][j] == c) {
		        count++;
		        j--;
		    }
		    score += getScore(count, c);

		    // 대각선 방향 (왼쪽 위에서 오른쪽 아래로) 체크
		    count = 1;
		    i = x + 1;
		    j = y + 1;
		    while (i < 15 && j < 15 && OmockBoard[i][j] == c) {
		        count++;
		        i++;
		        j++;
		    }
		    i = x - 1;
		    j = y - 1;
		    while (i >= 0 && j >= 0 && OmockBoard[i][j] == c) {
		        count++;
		        i--;
		        j--;
		    }
		    score += getScore(count, c);

		    // 대각선 방향 (오른쪽 위에서 왼쪽 아래로) 체크
		    count = 1;
		    i = x - 1;
		    j = y + 1;
		    while (i >= 0 && j < 15 && OmockBoard[i][j] == c) {
		        count++;
		        i--;
		        j++;
		    }
		    i = x + 1;
		    j = y - 1;
		    while (i < 15 && j >= 0 && OmockBoard[i][j] == c) {
		        count++;
		        i++;
		        j--;
		    }
		    score += getScore(count, c);

		    return score;
		}

		private int getScore(int count, int c) {
		    int score = 0;

		    if (count >= 5) {
		        score = 100; // 5목이 완성되면 최고 점수 부여
		    } else if (count == 4) {
		        if (c == 1) {
		            score = 90; // 상대방 4목 막기
		        } else {
		            score = 80; // AI 4목 만들기
		        }
		    } else if (count == 3) {
		        if (c == 1) {
		            score = 70; // 상대방 3목 막기
		        } else {
		            score = 60; // AI 3목 만들기
		        }
		    } else if (count == 2) {
		        if (c == 1) {
		            score = 50; // 상대방 2목 막기
		        } else {
		            score = 40; // AI 2목 만들기
		        }
		    } else if (count == 1) {
		        if (c == 1) {
		            score = 30; // 상대방 1목 막기
		        } else {
		            score = 20; // AI 1목 만들기
		        }
		    }

		    return score;
    }

	public P GameTree(int c, int limit) {
			
		return getPositionForMax(c, limit, 0);
	}

	private StoneDraw.P getPositionForMax(int c, int limit, int cur_score) {
		 StoneDraw.P result = new StoneDraw.P();
		    int maxScore = Integer.MIN_VALUE;

		    // 현재 상태에서 가능한 모든 수를 시도하여 최적의 수 찾기
		    for (int i = 0; i < 15; i++) {
		        for (int j = 0; j < 15; j++) {
		            if (OmockBoard[i][j] == 0) {
		                // 비어있는 위치에 돌 놓기
		                OmockBoard[i][j] = c;

		                // 점수 측정
		                int score = scoreVarEstimation(i, j, c);
		                int totalScore = cur_score + score;

		                if (limit > 1) {
		                    // limit이 남아있을 경우, 반대 플레이어의 최선의 수 선택
		                    StoneDraw.P nextPosition = getPositionForMin(3 - c, limit - 1, totalScore);

		                    // 최대 점수를 가지는 수 선택
		                    if (nextPosition.MAX > maxScore) {
		                        maxScore = nextPosition.MAX;
		                        result.i = i;
		                        result.j = j;
		                        result.MAX = maxScore;
		                        result.MIN = nextPosition.MIN;
		                    }
		                } else {
		                    // limit이 남아있지 않은 경우, 현재 점수로만 판단
		                    if (totalScore > maxScore) {
		                        maxScore = totalScore;
		                        result.i = i;
		                        result.j = j;
		                        result.MAX = maxScore;
		                        result.MIN = cur_score;
		                    }
		                }

		                // 돌 제거
		                OmockBoard[i][j] = 0;
		            }
		        }
		    }

		    return result;
	}



	private StoneDraw.P getPositionForMin(int c, int limit, int cur_score) {
		   StoneDraw.P result = new StoneDraw.P();
		    int maxScore = Integer.MIN_VALUE;

		    // 현재 상태에서 가능한 모든 수를 시도하여 최적의 수 찾기
		    for (int i = 0; i < 15; i++) {
		        for (int j = 0; j < 15; j++) {
		            if (OmockBoard[i][j] == 0) {
		                // 비어있는 위치에 돌 놓기
		                OmockBoard[i][j] = c;

		                // 점수 측정
		                int score = scoreVarEstimation(i, j, c);
		                int totalScore = cur_score + score;

		                if (limit > 1) {
		                    // limit이 남아있을 경우, 반대 플레이어의 최선의 수 선택
		                    StoneDraw.P nextPosition = getPositionForMin(3 - c, limit - 1, totalScore);

		                    // 최대 점수를 가지는 수 선택
		                    if (nextPosition.MAX > maxScore) {
		                        maxScore = nextPosition.MAX;
		                        result.i = i;
		                        result.j = j;
		                        result.MAX = maxScore;
		                        result.MIN = nextPosition.MIN;
		                    }
		                } else {
		                    // limit이 남아있지 않은 경우, 현재 점수로만 판단
		                    if (totalScore > maxScore) {
		                        maxScore = totalScore;
		                        result.i = i;
		                        result.j = j;
		                        result.MAX = maxScore;
		                        result.MIN = cur_score;
		                    }
		                }

		                // 돌 제거
		                OmockBoard[i][j] = 0;
		            }
		        }
		    }

		    return result;
	}

	public void AIStoneDraw() {
		int color = 2;
		if (StoneColor.equals(Color.BLACK)) {
			StoneColor = Color.WHITE;
			color = 2;
		} else {
			StoneColor = Color.BLACK;
			color = 1;
		}
		
		P next_P = GameTree(color, 1);
		
		
		int i = next_P.i;
		int j = next_P.j;
		OmockBoard[i][j] = color;

		Graphics g = contentPane.getGraphics();

		g.setColor(StoneColor);

		int X = i * 30 + 10 - STONE_SIZE / 2;
		int Y = j * 30 + 10 - STONE_SIZE / 2;
		g.fillOval(X, Y, STONE_SIZE, STONE_SIZE);

		if (checkNinLine(i, j, OmockBoard[i][j], 5)>0) {
			if (StoneColor.equals(Color.BLACK))
				JOptionPane.showMessageDialog(null, "Black Win");
			else
				JOptionPane.showMessageDialog(null, "White Win");
			System.exit(0);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Graphics g = contentPane.getGraphics();

		int i = (e.getX() - 10 + 15) / 30;
		int j = (e.getY() - 10 + 15) / 30;
		

		if (OmockBoard[i][j] != 0)
			return;

		if (StoneColor.equals(Color.BLACK)) {
			StoneColor = Color.WHITE;
			OmockBoard[i][j] = 2;
		} else {
			StoneColor = Color.BLACK;
			OmockBoard[i][j] = 1;
		}
		
		g.setColor(StoneColor);

		
		
		int X = i * 30 + 10 - STONE_SIZE / 2;
		int Y = j * 30 + 10 - STONE_SIZE / 2;
		g.fillOval(X, Y, STONE_SIZE, STONE_SIZE);

		if (checkNinLine(i, j, OmockBoard[i][j], 5) > 0) {
			if (StoneColor.equals(Color.BLACK))
				JOptionPane.showMessageDialog(null, "Black Win");
			else
				JOptionPane.showMessageDialog(null, "White Win");
			System.exit(0);
		}

		AIStoneDraw();

	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	public int checkNinLine(int x, int y, int c, int n) {
		int noOfNinLine = 0;

		int h = 1;
		for (int i = x + 1; i >= 0 && i < 15 && c == OmockBoard[i][y]; i++)
			h++;
		for (int i = x - 1; i >= 0 && i < 15 && c == OmockBoard[i][y]; i--)
			h++;
		if (h == n)
			noOfNinLine++;

		int v = 1;
		for (int i = y + 1; i >= 0 && i < 15 && c == OmockBoard[x][i]; i++)
			v++;
		for (int i = y - 1; i >= 0 && i < 15 && c == OmockBoard[x][i]; i--)
			v++;
		if (v == n)
			noOfNinLine++;

		int ld = 1;
		for (int i = 1; x + i >= 0 && x + i < 15 && y + i >= 0 && y + i < 15 && c == OmockBoard[x + i][y + i]; i++)
			ld++;
		for (int i = 1; x - i >= 0 && x - i < 15 && y - i >= 0 && y - i < 15 && c == OmockBoard[x - i][y - i]; i++)
			ld++;
		if (ld == n)
			noOfNinLine++;

		int rd = 1;
		for (int i = 1; x - i >= 0 && x - i < 15 && y + i >= 0 && y + i < 15 && c == OmockBoard[x - i][y + i]; i++)
			rd++;
		for (int i = 1; x + i >= 0 && x + i < 15 && y - i >= 0 && y - i < 15 && c == OmockBoard[x + i][y - i]; i++)
			rd++;
		if (rd == n)
			noOfNinLine++;

		return noOfNinLine;

	}

}


public class BasicFrame extends JFrame {

	private JPanel contentPane;
	final int STONE_SIZE = 26;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BasicFrame frame = new BasicFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BasicFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 480); // 15 by 15
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setColor(Color.BLACK);
				for (int i = 10; i < 450; i = i + 30) { // 10,10 시작 30씩 증가,
					g.drawLine(10, i, 460 - 30, i);
					g.drawLine(i, 10, i, 460 - 30);
				}
				
				int X = 7 * 30 + 10 - STONE_SIZE / 2;
				int Y = 7 * 30 + 10 - STONE_SIZE / 2;
				g.fillOval(X, Y, STONE_SIZE, STONE_SIZE);
			}
		};
		contentPane.addMouseListener(new StoneDraw(contentPane));
		contentPane.setBackground(new Color(184, 134, 11));
		setContentPane(contentPane);
	}
}
