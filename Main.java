import java.util.*;
import java.io.*;
import java.time.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static final String USER_FILE = "users.txt";
    static final String SCORE_FILE = "scoreboard.csv";
    static final String CUSTOM_FILE = "custom_questions.csv";

    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[32m";
    static final String RED = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) throws Exception {
        System.out.println(BLUE + "🎮 WELCOME TO THE QUIZ APP 🎮" + RESET);
        String username = loginMenu();

        while (true) {
            System.out.println("\n======================");
            System.out.println("      MAIN MENU");
            System.out.println("======================");
            System.out.println("1️⃣ Play Quiz");
            System.out.println("2️⃣ Daily Challenge");
            System.out.println("3️⃣ View Leaderboard");
            System.out.println("4️⃣ View My History");
            System.out.println("5️⃣ Add Custom Question");
            System.out.println("6️⃣ Logout");
            System.out.print("Enter choice: ");

            int opt = readInt();

            switch (opt) {
                case 1: playQuiz(username, false); break;
                case 2: playQuiz(username, true); break;
                case 3: showLeaderboard(); break;
                case 4: showUserHistory(username); break;
                case 5: addCustomQuestion(); break;
                case 6: 
                    System.out.println(GREEN + "\n👋 Logged out successfully." + RESET);
                    username = loginMenu();
                    break;
                default: System.out.println(RED + "❌ Invalid choice." + RESET); break;
            }
        }
    }

    // ----------------- LOGIN & REGISTRATION -----------------
    static String loginMenu() throws IOException {
        while (true) {
            System.out.println("\n1️⃣ Login  2️⃣ Register");
            int choice = readInt();
            sc.nextLine();
            if (choice == 1) {
                String user = loginUser();
                if (user != null) return user;
            } else if (choice == 2) {
                registerUser();
            } else {
                System.out.println(RED + "❌ Invalid choice." + RESET);
            }
        }
    }

    static void registerUser() throws IOException {
        System.out.print("\nEnter new username: ");
        String user = sc.nextLine();
        System.out.print("Enter new password: ");
        String pass = sc.nextLine();

        if (userExists(user)) {
            System.out.println(YELLOW + "⚠️ Username already exists!" + RESET);
            return;
        }

        try (FileWriter fw = new FileWriter(USER_FILE, true)) {
            fw.write(user + "," + pass + "\n");
        }
        System.out.println(GREEN + "✅ Registration successful!" + RESET);
    }

    static String loginUser() throws IOException {
        System.out.print("\nUsername: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        File f = new File(USER_FILE);
        if (!f.exists()) {
            System.out.println(YELLOW + "⚠️ No users found! Please register first." + RESET);
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts[0].equals(user) && parts[1].equals(pass)) {
                    System.out.println(GREEN + "✅ Welcome back, " + user + "!" + RESET);
                    return user;
                }
            }
        }
        System.out.println(RED + "❌ Invalid credentials!" + RESET);
        return null;
    }

    static boolean userExists(String user) throws IOException {
        File f = new File(USER_FILE);
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(",", 2)[0].equals(user)) return true;
            }
        }
        return false;
    }

    // ----------------- QUIZ -----------------
    static void playQuiz(String name, boolean dailyChallenge) throws IOException {
        String[] questions;
        String[][] options;
        char[] answers;
        String category;

        if (dailyChallenge) {
            questions = new String[]{"💥 Daily Challenge: Which planet is known as the Red Planet?"};
            options = new String[][]{{"A) Earth","B) Mars","C) Jupiter","D) Venus"}};
            answers = new char[]{'B'};
            category = "Daily";
            System.out.println(PURPLE + "\n🔥 Daily Challenge! Answer correctly for bonus points!" + RESET);
        } else {
            System.out.println("\nChoose your quiz category:");
            System.out.println("1️⃣ Java Quiz  2️⃣ General Knowledge  3️⃣ Math Quiz  4️⃣ Custom Questions");
            int choice = readInt();

            switch (choice) {
                case 1: category="Java"; break;
                case 2: category="GK"; break;
                case 3: category="Math"; break;
                case 4: category="Custom"; break;
                default: 
                    System.out.println(RED + "❌ Invalid choice!" + RESET); 
                    return;
            }

            if (choice == 1) {
                questions = new String[]{"Who developed Java?","Which keyword is used to inherit a class?",
                        "What is used to handle exceptions?","Which method is entry point of Java programs?",
                        "Which data type stores true/false values?"};
                options = new String[][]{
                        {"A) Bjarne Stroustrup","B) James Gosling","C) Dennis Ritchie","D) Guido van Rossum"},
                        {"A) inherit","B) implements","C) extends","D) inherits"},
                        {"A) try-catch","B) if-else","C) switch","D) loop"},
                        {"A) begin()","B) start()","C) main()","D) run()"},
                        {"A) int","B) boolean","C) char","D) float"}};
                answers = new char[]{'B','C','A','C','B'};
            } else if (choice == 2) {
                questions = new String[]{"Capital of France?","Largest ocean on Earth?",
                        "Fastest land animal?","Which country invented paper?","Smallest continent?"};
                options = new String[][]{
                        {"A) Berlin","B) Paris","C) Rome","D) Madrid"},
                        {"A) Indian","B) Pacific","C) Atlantic","D) Arctic"},
                        {"A) Cheetah","B) Lion","C) Tiger","D) Leopard"},
                        {"A) Japan","B) China","C) India","D) Egypt"},
                        {"A) Asia","B) Europe","C) Australia","D) Africa"}};
                answers = new char[]{'B','B','A','B','C'};
            } else if (choice == 3) {
                questions = new String[]{"5 + 3 = ?","Square root of 49?","15 ÷ 3 = ?","What is 7 × 6?","10% of 200 = ?"};
                options = new String[][]{
                        {"A) 5","B) 8","C) 9","D) 6"},
                        {"A) 6","B) 7","C) 8","D) 9"},
                        {"A) 4","B) 5","C) 6","D) 7"},
                        {"A) 42","B) 48","C) 36","D) 30"},
                        {"A) 10","B) 15","C) 20","D) 25"}};
                answers = new char[]{'B','B','B','A','C'};
            } else {
                List<String> qList = new ArrayList<>();
                List<String[]> oList = new ArrayList<>();
                List<Character> aList = new ArrayList<>();
                File f = new File(CUSTOM_FILE);
                if (f.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] parts = line.split(",",6);
                            qList.add(parts[0]);
                            oList.add(new String[]{parts[1],parts[2],parts[3],parts[4]});
                            aList.add(parts[5].charAt(0));
                        }
                    }
                }
                if (qList.isEmpty()) {
                    System.out.println(YELLOW + "⚠️ No custom questions found!" + RESET);
                    return;
                }
                questions = qList.toArray(new String[0]);
                options = oList.toArray(new String[0][]);
                answers = new char[aList.size()];
                for (int i=0;i<aList.size();i++) answers[i]=aList.get(i);
            }
        }

        // Play Quiz
        int totalQ = questions.length;
        int score = 0;
        int hintsLeft = 1;

        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < totalQ; i++) indexList.add(i);
        Collections.shuffle(indexList);

        for (int idx : indexList) {
            System.out.println(PURPLE + "\n🎯 Q: " + questions[idx] + RESET);
            for (String opt : options[idx]) System.out.println(opt);

            char ans = ' ';
            long startTime = System.currentTimeMillis();
            while (true) {
                System.out.print("Answer (A/B/C/D) or H for hint: ");
                String input = sc.next().toUpperCase();
                long elapsed = (System.currentTimeMillis()-startTime)/1000;
                if (!dailyChallenge && elapsed>15) {
                    System.out.println(RED + "⏰ Time's up! Moving to next question." + RESET);
                    break;
                }

                if (input.equals("H")) {
                    if (hintsLeft>0) { provideHint(options[idx],answers[idx]); hintsLeft--; continue; }
                    else { System.out.println(YELLOW+"⚠️ No hints left!"+RESET); continue; }
                }
                if (input.length()==1 && "ABCD".contains(input)) { ans=input.charAt(0); break; }
                System.out.println(RED+"❌ Enter A/B/C/D or H only!"+RESET);
            }

            if (ans==answers[idx]) {
                System.out.println(GREEN+"✅ Correct!"+RESET);
                score += dailyChallenge ? 2 : 1;
            } else if(ans!=' ') {
                System.out.println(RED+"❌ Wrong! Correct: "+answers[idx]+RESET);
            }
        }

        double percent = (score / (double) totalQ) * 100;
        String grade = (percent>=90)?"A+":(percent>=75)?"A":(percent>=60)?"B":(percent>=40)?"C":"Fail";

        System.out.println(BLUE+"\n📊 Result Summary"+RESET);
        System.out.println("---------------------");
        System.out.println("User: "+name);
        System.out.println("Category: "+category);
        System.out.println("Score: "+score+"/"+totalQ);
        System.out.println("Percentage: "+String.format("%.2f", percent)+"%");
        System.out.println("Grade: "+grade);

        try (FileWriter fw = new FileWriter(SCORE_FILE,true)) {
            fw.write(String.format("%s,%s,%d,%d,%s,%.2f,%s\n",name,category,score,totalQ,grade,percent,LocalDateTime.now()));
        }
        System.out.println(GREEN+"✅ Score saved!"+RESET);
    }

    static void provideHint(String[] opts, char correct) {
        System.out.print(YELLOW+"💡 Hint: Eliminated options: ");
        int removed=0;
        for(String s: opts) {
            if(s.charAt(0)!=correct && removed<2) { System.out.print(s+" "); removed++; }
        }
        System.out.println(RESET);
    }

    // ----------------- LEADERBOARD -----------------
    static void showLeaderboard() throws IOException {
        File f = new File(SCORE_FILE);
        if(!f.exists()) { System.out.println(YELLOW+"\n⚠️ No scores yet!"+RESET); return; }

        Map<String,Integer> userMaxScore = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while((line=br.readLine())!=null) {
                String[] parts = line.split(",",7);
                String name = parts[0];
                int score = Integer.parseInt(parts[2]);
                userMaxScore.put(name, Math.max(userMaxScore.getOrDefault(name,0),score));
            }
        }

        List<Map.Entry<String,Integer>> list = new ArrayList<>(userMaxScore.entrySet());
        list.sort((a,b)->b.getValue()-a.getValue());

        System.out.println(BLUE+"\n🏆 TOP 5 PLAYERS 🏆"+RESET);
        int limit = Math.min(5, list.size());
        for(int i=0;i<limit;i++) {
            Map.Entry<String,Integer> e = list.get(i);
            System.out.println((i+1)+". "+e.getKey()+" : "+e.getValue());
        }
    }

    // ----------------- VIEW USER HISTORY -----------------
    static void showUserHistory(String name) throws IOException {
        File f = new File(SCORE_FILE);
        if(!f.exists()) { System.out.println(YELLOW+"\n⚠️ No scores yet!"+RESET); return; }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            System.out.println(BLUE+"\n📜 "+name+"'s History:"+RESET);
            while((line=br.readLine())!=null) {
                if(line.startsWith(name+",")) {
                    String[] parts = line.split(",",7);
                    System.out.println("Category: "+parts[1]+" | Score: "+parts[2]+"/"+parts[3]+" | Grade: "+parts[4]+" | %: "+parts[5]+" | Time: "+parts[6]);
                }
            }
        }
    }

    // ----------------- CUSTOM QUESTIONS -----------------
    static void addCustomQuestion() throws IOException {
        System.out.print("\nEnter question: ");
        sc.nextLine();
        String q = sc.nextLine();
        String[] opts = new String[4];
        for(int i=0;i<4;i++) {
            System.out.print("Option "+(char)('A'+i)+": ");
            opts[i] = sc.nextLine();
        }
        System.out.print("Enter correct answer (A/B/C/D): ");
        char ans = sc.next().toUpperCase().charAt(0);

        try (FileWriter fw = new FileWriter(CUSTOM_FILE,true)) {
            fw.write(q+","+opts[0]+","+opts[1]+","+opts[2]+","+opts[3]+","+ans+"\n");
        }
        System.out.println(GREEN+"✅ Custom question added!"+RESET);
    }

    // ----------------- UTILS -----------------
    static int readInt() {
        while(true) {
            String s = sc.next();
            try { return Integer.parseInt(s); }
            catch(Exception e) { System.out.print(RED+"❌ Enter a valid number: "+RESET); }
        }
    }
}
