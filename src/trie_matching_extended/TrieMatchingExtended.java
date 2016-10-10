import java.io.*;
import java.util.*;

class NodeExtended {
    public static final int Letters = 4;
    public static final int NA = -1;
    public int next[];
    public boolean patternEnd;

    NodeExtended() {
        next = new int[Letters];
        Arrays.fill(next, NA);
        patternEnd = false;
    }
}

public class TrieMatchingExtended implements Runnable {
    int letterToIndex(char letter) {
        switch (letter) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'G':
                return 2;
            case 'T':
                return 3;
            default:
                assert (false);
                return NodeExtended.NA;
        }
    }

    char indexToLetter(int index) {
        switch (index) {
            case 0:
                return 'A';
            case 1:
                return 'C';
            case 2:
                return 'G';
            case 3:
                return 'T';
            default:
                assert (false);
                return '?';
        }
    }

    List<Integer> solve(String text, int n, List<String> patterns) {
        List<Integer> result = new ArrayList<Integer>();

        // write your code here
        List<NodeExtended> trie = buildTrieExtended(patterns);

        int k = 0;
        NodeExtended root = trie.get(0);

        while (!text.isEmpty()) {
            int match = -1;
            char currentSymbol = text.charAt(0);
            NodeExtended currentNode = root;
            int l = 0;

            while (true) {
                // the current node is where one of the patterns ends;
                // it means positive match
                if (currentNode.patternEnd) {
                    match = k;

                    break;
                } else if (currentNode.next[letterToIndex(currentSymbol)] != NodeExtended.NA) {
                    // we can advance one node down
                    // and check if we reached the end of the Text string
                    currentNode = trie.get(currentNode.next[letterToIndex(currentSymbol)]);

                    // we didn't: getting the next symbol
                    if (l + 1 < text.length()) {
                        currentSymbol = text.charAt(++l);
                    } else {
                        // we did: check if the current node is where one of the patterns ends;
                        // it means positive match
                        if (currentNode.patternEnd) {
                            match = k;
                        }

                        // otherwise we failed to match any pattern;
                        // it means negative match
                        break;
                    }
                } else {
                    break;
                }
            }

            if (match != -1) {
                result.add(match);
            }

            // advance one position to the end of the Text string
            k++;
            text = text.substring(1);
        }

        return result;
    }

    List<NodeExtended> buildTrieExtended(List<String> patterns) {
        List<NodeExtended> trie = new ArrayList<>();

        trie.add(new NodeExtended());

        for (String pattern : patterns) {
            int currentNodeIndex = 0;
            int patternLength = pattern.length();

            for (int i = 0; i < patternLength; i++) {
                char currentSymbol = pattern.charAt(i);
                NodeExtended currentNode = trie.get(currentNodeIndex);
                int currentSymbolIndex = letterToIndex(currentSymbol);

                if (currentNode.next[currentSymbolIndex] == NodeExtended.NA) {
                    NodeExtended newNode = new NodeExtended();
                    trie.add(newNode);
                    int newNodeIndex = trie.size() - 1;
                    currentNode.next[currentSymbolIndex] = newNodeIndex;
                    currentNodeIndex = newNodeIndex;
                } else {
                    currentNodeIndex = currentNode.next[currentSymbolIndex];
                }

                if (i == patternLength - 1) {
                    NodeExtended patternEndNode = trie.get(currentNodeIndex);
                    patternEndNode.patternEnd = true;
                }
            }
        }

//        print(trie);
        return trie;
    }

    public void print(List<NodeExtended> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            NodeExtended node = trie.get(i);
            for (int j = 0; j < 4; ++j) {
                int entry = node.next[j];
                if (entry != NodeExtended.NA) {
                    System.out.println(i + "->" + entry + ":" + indexToLetter(j) + "(" + node.patternEnd + ")");
                }
            }
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String text = in.readLine();
            int n = Integer.parseInt(in.readLine());
            List<String> patterns = new ArrayList<String>();
            for (int i = 0; i < n; i++) {
                patterns.add(in.readLine());
            }

            List<Integer> ans = solve(text, n, patterns);

            for (int j = 0; j < ans.size(); j++) {
                System.out.print("" + ans.get(j));
                System.out.print(j + 1 < ans.size() ? " " : "\n");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new TrieMatchingExtended()).start();
    }
}