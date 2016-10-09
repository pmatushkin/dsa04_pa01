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
        int textLength = text.length();
        NodeExtended root = trie.get(0);

        while (k < textLength) {
            NodeExtended v = root;
            int l = k;
            boolean isSubPatternFound = false;

            while (true) {
                // the current node is a leaf node;
                // the found pattern is not empty;
                // it means positive match
                if (isNodeLeaf(v) || isSubPatternFound) {
                    result.add(k);
                    break;
                }

                // the Text string ended, but we are not at a leaf node;
                // it means negative match
                if (l >= textLength) {
                    break;
                }

                char symbol = text.charAt(l);
                int symbolIndex = letterToIndex(symbol);

                if (v.next[symbolIndex] != NodeExtended.NA) {
                    // advancing to the next symbol in Text,
                    // and to the next node in trie
                    isSubPatternFound = v.patternEnd;
                    l++;
                    v = trie.get(v.next[symbolIndex]);
                } else {
                    // cannot find follow-up nodes matching the current symbol;
                    // it means negative match
                    break;
                }
            }

            k++;
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

                if ((i == patternLength - 1) && (!isNodeLeaf(currentNode))) {
                    currentNode.patternEnd = true;
                }
            }
        }

        print(trie);
        return trie;
    }

    boolean isNodeLeaf(NodeExtended node) {
        return node.next[0] == NodeExtended.NA
                && node.next[1] == NodeExtended.NA
                && node.next[2] == NodeExtended.NA
                && node.next[3] == NodeExtended.NA;
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
