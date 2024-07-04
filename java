import java.util.Random;
import java.util.Scanner;
 
// 天赋枚举
enum Talent {
    ARTISTIC,
    MATHEMATICAL,
    ATHLETIC,
    SOCIAL;
 
    @Override
    public String toString() {
        switch (this) {
            case ARTISTIC:
                return "艺术";
            case MATHEMATICAL:
                return "数学";
            case ATHLETIC:
                return "运动";
            case SOCIAL:
                return "社交";
            default:
                return "未知";
        }
    }
}
 
// 角色类
class Character {
    String name; // 角色名字
    int beauty; // 颜值
    int intelligence; // 智力
    int strength; // 体质
    int background; // 家境
    Talent talent; // 特有天赋
 
    public Character(String name) { // 增加名字参数
        Random rand = new Random();
        this.name = name;
        beauty = rand.nextInt(100) + 1;
        intelligence = rand.nextInt(100) + 1;
        strength = rand.nextInt(100) + 1;
        background = rand.nextInt(100) + 1;
        talent = Talent.values()[rand.nextInt(Talent.values().length)];
    }
 
    // 显示角色信息，包括名字
    public void displayAttributes() {
        System.out.println("角色名字: " + name);
        System.out.println("颜值: " + beauty);
        System.out.println("智力: " + intelligence);
        System.out.println("体质: " + strength);
        System.out.println("家境: " + background);
        System.out.println("天赋: " + talent.toString());
    }
}
 
// 事件类
class Event {
    String description; // 事件描述
    // 事件影响的枚举，定义事件可能影响的属性类型
    enum ImpactType {
        BEAUTY,
        INTELLIGENCE,
        STRENGTH,
        BACKGROUND
    }
 
    ImpactType impactType; // 事件将影响哪种属性
    int impactValue; // 事件对属性的影响值
 
    public Event(String description, ImpactType impactType, int impactValue) {
        this.description = description;
        this.impactType = impactType;
        this.impactValue = impactValue;
    }
 
	// 事件影响角色的方法
	public void affect(Character character) {
		if (impactType != null) {
			switch (impactType) {
				case BEAUTY:
                // 假设正数为提升，负数为下降
					character.beauty = Math.max(0, character.beauty + impactValue);
					break;
				case INTELLIGENCE:
					character.intelligence = Math.max(0, character.intelligence + impactValue);
					break;
				case STRENGTH:
					character.strength = Math.max(0, character.strength + impactValue);
					break;
				case BACKGROUND:
					character.background = Math.max(0, character.background + impactValue);
					break;
			}
		}
	}	
    // 显示事件描述的方法
    public void displayEvent() {
        System.out.println(description);
    }
}
 
// 创造一个死亡事件的类
class DeathEvent extends Event {
    public DeathEvent(String description) {
        super(description, null, 0); // DeathEvent没有影响任何属性
    }
 
    @Override
    public void affect(Character character) {
        // 此方法在游戏引擎处理死亡事件时会被调用，此处不做任何事情
    }
}
 
// 游戏引擎
class GameEngine {
    Character character;
    boolean isGameOver;
    Event[] events; // 事件数组
 
    public GameEngine() {
        isGameOver = false;
    }
 
    private void initEvents() {
        events = new Event[]{
            new Event("顺风顺水的一天，你的颜值略微提升。", Event.ImpactType.BEAUTY, 2),
            new Event("读书学习，智力略微提升。", Event.ImpactType.INTELLIGENCE, 3),
            new Event("健身房锻炼，体质有所增强。", Event.ImpactType.STRENGTH, 5),
            new Event("家里破产，家境降低。", Event.ImpactType.BACKGROUND, 10),
            // 可以添加其他事件...
            new DeathEvent("不幸地，角色得了一种无法治愈的疾病，不久后去世了。")
        };
    }
 
    private Event randomEvent() {
        Random rand = new Random();
        return events[rand.nextInt(events.length)];
    }
 
     public void startGame() {
        initEvents(); // 初始化事件
        Scanner scanner = new Scanner(System.in);
        System.out.print("请问您的角色叫什么名字？: ");
        String name = scanner.nextLine().trim(); // 玩家输入名字
        character = new Character(name);
 
        System.out.println("角色 [" + character.name + "] 创建成功！角色信息如下：");
        character.displayAttributes();
        System.out.println();
 
        while (!isGameOver) {
            // 在这里等待用户输入，让游戏节奏减慢，让玩家有时间阅读事件描述
            System.out.println("准备好了吗？输入任何非零数字然后回车继续，或者输入 0 退出游戏。");
            int input;
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
            } else {
                System.out.println("输入错误，请输入数字。");
                scanner.next();  // 清除错误的输入
                continue;
            }
            scanner.nextLine(); // 清除输入行的换行符
 
            if (input == 0) {
                System.out.println("感谢您的游玩，再见！");
                break;
            }
            
            Event event = randomEvent();
            event.displayEvent();
            triggerEvent(event);
            
            if (event instanceof DeathEvent) {
                isGameOver = true;
                System.out.println(character.name + " 已经去世...");
            } else {
                System.out.println("事件影响后，当前角色属性如下：");
                character.displayAttributes();
                System.out.println();
            }
        }
        scanner.close();
    }
 
    public void triggerEvent(Event event) {
        event.affect(character);
    }
}
 
// 主程序
public class LifeSimulationGame {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.startGame();
    }
}