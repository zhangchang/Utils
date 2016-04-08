
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.FileOutputStream;

public class Sudou {
	/**
	 * 类入口点
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:47:38
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param args
	 */

	public static int N = 4;

	public static void main(String[] args) {
		Sudou sd = new Sudou(N * N);
		sd.init(N * N);
		sd.generateRandom(1);
	}

	public Sudou() {
		init(N);
	}

	public Sudou(int num) {
		init(num);
	}

	private int fieldNum;
	private int[] layout = null;// 布局
	private int[] outbytes = null;
	private int curPos;// 当前处理的布局位置
	private Random random = new Random();
	private int[] ansPosArr = null;// 每个布局位置解空间使用标识（指向下一次要处理的解）
	private int[][] ansArr = null;// 记录每个位置的解空间
	private boolean randomLayout = false;

	public void setRandomLayout(boolean r) {
		randomLayout = r;
	}

	private void init(int num) {
		curPos = 0;
		fieldNum = num;
		if (layout == null)
			layout = new int[num * num];
		if (outbytes == null)
			outbytes = new int[(num * num + 1) / 2];
		if (ansPosArr == null)
			ansPosArr = new int[num * num];
		// 用来记录布局中某个位置的可能解
		if (ansArr == null)
			ansArr = new int[num * num][num];
		for (int i = 0; i < num * num; i++) {
			layout[i] = -1;// 将布局全部设置为未填状态
			ansPosArr[i] = 0;// 用来记录解的位置，回溯时从这个位置往后处理
			for (int j = 0; j < num; j++)
				ansArr[i][j] = -1;// 初始化为无解，程序运行中动态求取
		}
	}

	public void outData() {
		for (int i = 0; i < fieldNum * fieldNum; i++) {
			if (i % (N * N) == 0)
				System.out.println();
			// System.out.print((String.valueOf(i).length() == 1 ? ("0" + i +
			// ":")
			// : (i + ":"))
			// + (layout[i] != -1 ? layout[i] : " ") + ";");
			System.out.print((layout[i] != -1 ? (layout[i] + 1) : " ") + ",");
		}
	}

	/**
	 * 以文本方式输出布局到文件
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:48:37
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param fw
	 */
	public void outData(FileWriter fw) {
		try {
			for (int i = 0; i < fieldNum * fieldNum; i++) {
				fw.write(String.valueOf(layout[i] + 1));
				if ((i + 1) % fieldNum == 0)
					fw.write("/n");
			}
			fw.write("/n");
		} catch (Exception e) {
		}
	}

	/**
	 * 以二进制方式输出布局
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:49:29
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param fos
	 */
	public void outData(FileOutputStream fos) {
		int pos = 0;
		boolean bEven = true;
		// 两个格合并到一个字节中存储，节省一半的空间。
		for (int i = 0; i < fieldNum * fieldNum; i++) {
			if (bEven) {
				outbytes[pos] = (byte) ((layout[i] + 1) << 4);
			} else {
				outbytes[pos] |= (layout[i] + 1) & 0xf;
				pos++;
			}
			bEven = !bEven;
		}
		try {
			//TODO:fix
			//fos.write(outbytes);
		} catch (Exception e) {
		}
	}

	/**
	 * 返回指定位置的可用解
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:49:50
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param pos
	 */
	private void getAnswer(int pos) {
		for (byte i = 0; i < fieldNum; i++)
			ansArr[pos][i] = i;// 假定包含所有解
		// 去除已经包含的
		int x = pos / fieldNum, y = pos % fieldNum;
		for (int i = 0; i < fieldNum; i++) {
			if (layout[i * fieldNum + y] != -1)
				ansArr[pos][layout[i * fieldNum + y]] = -1;// 去除列中包含的元素
			if (layout[x * fieldNum + i] != -1)
				ansArr[pos][layout[x * fieldNum + i]] = -1;// 去除行中包含的元素
		}
		int subnum = (int) Math.sqrt(fieldNum);
		int x2 = x / subnum, y2 = y / subnum;
		// boolean bOver = false;//这个优化应该是没有问题的
		for (int i = x2 * subnum; i < subnum + x2 * subnum; i++) {
			// if (bOver)
			// break;
			for (int j = y2 * subnum; j < subnum + y2 * subnum; j++) {
				if (layout[i * fieldNum + j] != -1)
					ansArr[pos][layout[i * fieldNum + j]] = -1;// 去小方格中包含的元素
			}
		}
		if (randomLayout == true)
			dealAnswer(pos);
		// outData();
		// System.out.print("/n位置：" + curPos + " 可用解：");
		// for (int i = 0; i < fieldNum; i++) {
		// if (ansArr[pos][i] != -1)
		// System.out.print(ansArr[pos][i] + ";");
		// }
		// System.out.println("[" + ansPosArr[curPos] + "]");
		// return list;
	}

	/**
	 * 可用解随机排序
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:50:04
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param pos
	 */
	private void dealAnswer(int pos) {
		// 随机调整一下顺序
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < fieldNum; i++)
			list.add(ansArr[pos][i]);
		int rdm = 0, idx = 0;
		while (list.size() != 0) {
			rdm = Math.abs(random.nextInt()) % list.size();
			ansArr[pos][idx] = list.get(rdm);
			list.remove(rdm);
			idx++;
		}
		list = null;
	}

	/**
	 * 取解的数量
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:50:20
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param pos
	 * @return
	 */
	private int getAnswerCount(int pos) {
		// 计算可用解的数量
		int count = 0;
		for (int i = 0; i < fieldNum; i++)
			if (ansArr[pos][i] != -1)
				count++;
		return count;
	}

	/**
	 * 取指定位置、第几个解
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:50:31
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param fieldPos
	 * @param ansPos
	 * @return
	 */
	private int getAnswerNum(int fieldPos, int ansPos) {
		// 返回指定布局方格中指定位置的解
		int cnt = 0;
		for (int i = 0; i < fieldNum; i++) {
			// 找到指定位置的解，返回
			if (cnt == ansPos && ansArr[fieldPos][i] != -1)
				return ansArr[fieldPos][i];
			if (ansArr[fieldPos][i] != -1)
				cnt++;// 是解，调整计数器
		}
		return -1;// 没有找到，逻辑没有问题的话，应该不会出现这个情况
	}

	/**
	 * 打开文件，用来输出布局
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:50:48
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param name
	 * @return
	 */
	private FileWriter openFileWriter(String name) {
		try {
			return new FileWriter(name);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 打开流，用来输出布局
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:50:55
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param name
	 * @return
	 */
	private FileOutputStream openFileStream(String name) {
		try {
			return new FileOutputStream(name);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 生成布局，这个是按顺序生成的。
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-14--下午12:49:02
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param layoutCount
	 *            要生成的布局数，如果为-1，表示要全部生成。
	 */
	public long generate(long layoutCount) {
		// FileWriter out = openFileWriter("layout.txt");
		// 如果要保存布局，把这个注释打开
		// FileOutputStream out = openFileStream("layout.bin");
		curPos = 0;
		long count = 0;
		while (count < layoutCount || layoutCount == -1) {
			if (ansPosArr[curPos] == 0)
				getAnswer(curPos);// 如果这个位置没有被回溯过，就不用重新计算解空间
			int ansCount = getAnswerCount(curPos);
			if (ansCount == ansPosArr[curPos] && curPos == 0)
				break;// 全部回溯完毕
			if (ansCount == 0) {
				ansPosArr[curPos] = 0;// 无可用解，应该就是0
				// System.out.println("无可用解，回溯！");
				curPos--;
				layout[curPos] = -1;
				continue;
			}
			// 可用解用完
			else if (ansPosArr[curPos] == ansCount) {
				// System.out.println("可用解用完，回溯！");
				ansPosArr[curPos] = 0;
				curPos--;
				layout[curPos] = -1;
				continue;
			} else {
				// 返回指定格格中，第几个解
				layout[curPos] = getAnswerNum(curPos, ansPosArr[curPos]);
				// System.out.println("位置："+curPos+" 填写："+layout[curPos]);
				ansPosArr[curPos]++;
				curPos++;
			}
			if (fieldNum * fieldNum == curPos) {
				System.out.print("/n========" + count + "========");
				outData();
				System.out.println();
				// if (out != null)
				// outData(out);
				count++;
				curPos--;
				layout[curPos] = -1;// 最后位置清空
				ansPosArr[curPos] = 1;// 解位置标识请零//人为促使继续回溯
			}
		}
		// try {
		// out.close();
		// } catch (Exception e) {
		// }
		// System.out.println("处理完毕！共生成：" + count);
		return count;
	}

	/**
	 * 以随机的方式生成布局
	 *
	 * <br/>
	 * 版本：V1.0 作者：wallimn 时间：2009-1-15--下午08:54:10
	 * 博客：http://blog.csdn.net/wallimn
	 *
	 * @param count
	 */
	public void generateRandom(int count) {
		setRandomLayout(true);
		for (int i = 0; i < count; i++) {
			init(N * N);
			generate(1);
		}
	}
}