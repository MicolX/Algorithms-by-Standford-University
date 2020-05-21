import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;

import java.util.HashMap;


public class BaseballElimination {

    private HashMap<String, Integer> teams;
    private int[] wins;
    private int[] loss;
    private int[] remains;
    private int[][] games;
    private Bag<String> certificate;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        String[] lines = in.readAllLines();

        wins = new int[lines.length-1];
        loss = new int[lines.length-1];
        remains = new int[lines.length-1];
        teams = new HashMap<>();
        int length = lines[1].trim().split(" +").length-4;
        games = new int[length][length];

        int count = 0;

        for (String s : lines) {
            if (s.equals(lines[0])) continue;
            else {
                String[] words = s.trim().split(" +");
                teams.put(words[0], count);
                wins[count] = Integer.parseInt(words[1]);
                loss[count] = Integer.parseInt(words[2]);
                remains[count] = Integer.parseInt(words[3]);


                for (int i = 4; i < words.length; i++) {
                    games[count][i-4] = Integer.parseInt(words[i]);
                }
                count++;
            }
        }

    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[teams.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return loss[teams.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return remains[teams.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return games[teams.get(team1)][teams.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {

        boolean out = false;
        certificate = new Bag<>();
        int targetNumber = teams.get(team);
        if (teams.size() == 1) return false;

        int start;
        int threshold = wins[targetNumber] + remains[targetNumber];

        if (!teams.containsKey(team) || team == null) throw new java.lang.IllegalArgumentException();
        else start = targetNumber;


        for (int i : teams.values()) {
            if (i == start) continue;
            else {
                if (wins[start] + remains[start] < wins[i]) {
                    for (String s : teams.keySet()) if (teams.get(s) == i) certificate.add(s);
                    out = true;
                }
            }
        }


        // build network
        String[] tableForMap = new String[teams.size()];
        for (String key : teams.keySet()) tableForMap[teams.get(key)] = key;

        int[] tableForNet = new int[teams.size() - certificate.size()];
        int count = 0;
        for (int a = 0; a < tableForMap.length; a++) {
            boolean jud = true;
            for (String cer : certificate) {
                if (tableForMap[a].equals(cer)) jud = false;
            }
            if (jud) {
                tableForNet[count++] = a;
            }
        }

        int startInNet = 0;
        for (int i = 0; i < tableForNet.length; i++) if (tableForNet[i] == start) startInNet = i;

        int end = vertices(tableForNet.length) - 1;
        FlowNetwork network = new FlowNetwork(end+1);
        int middle = end - 1;
        for (int k = tableForNet.length - 1; k > 0; k--) {
            if (k == startInNet) continue;
            else {
                for (int j = k - 1; j >= 0; j--) {
                    if (j == startInNet) continue;
                    else {
                        network.addEdge(new FlowEdge(startInNet, middle, games[tableForNet[j]][tableForNet[k]], 0));
                        network.addEdge(new FlowEdge(middle, k, Double.POSITIVE_INFINITY, 0));
                        network.addEdge(new FlowEdge(middle, j, Double.POSITIVE_INFINITY, 0));
                    }
                    middle--;
                }
            }
            network.addEdge(new FlowEdge(k, end, threshold - wins[tableForNet[k]], 0));
        }
        network.addEdge(new FlowEdge(0, end, threshold - wins[tableForNet[0]], 0));
        //StdOut.println(network);

        // find maxflow and mincut
        FordFulkerson FF = new FordFulkerson(network, start, end);
        for (FlowEdge edge : network.adj(start)) {
            if (edge.flow() != edge.capacity()) out = true;
        }

        if (out) {
            for (int n = 0; n < tableForNet.length; n++) {
                if (FF.inCut(n) && tableForNet[n] != targetNumber) certificate.add(tableForMap[tableForNet[n]]);
            }
        }



        return out;

    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (isEliminated(team)) return certificate;
        else return null;
    }

    private int vertices(int t) {
        int count = t + 1;
        for (int i = t - 2; i > 0; i--) {
            count += i;
        }
        return count;
    }


    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

//        // 测试读取有没有问题
//        for (String team : division.teams.keySet()) {
//            int number = division.teams.get(team);
//            StdOut.print(team+" ");
//            StdOut.printf("%2d %2d %2d ", division.wins[number], division.loss[number], division.remains[number]);
//            for (int i : division.games[number]) StdOut.printf("%2d ", i);
//            StdOut.println();
//        }
    }
}
