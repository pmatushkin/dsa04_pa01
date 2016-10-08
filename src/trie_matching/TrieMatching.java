import java.io.*;
import java.util.*;

class Node {
    public static final int Letters = 4;
    public static final int NA = -1;
    public int next[];

    Node() {
        next = new int[Letters];
        Arrays.fill(next, NA);
    }
}

public class TrieMatching implements Runnable {
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
                return Node.NA;
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
        List<Node> trie = buildTrie(patterns);

        int k = 0;
        int textLength = text.length();
        Node root = trie.get(0);

        while (k < textLength) {
            String pattern = "";
            Node v = root;
            int l = k;

            while (true) {
                // the current node is a leaf node;
                // the found pattern is not empty;
                // it means positive match
                if (isNodeLeaf(v)) {
                    if (pattern.length() > 0) {
                        result.add(k);
                        break;
                    }
                }

                // the Text string ended, but we are not at a leaf node;
                // it means negative match
                if (l >= textLength) {
                    break;
                }

                char symbol = text.charAt(l);
                int symbolIndex = letterToIndex(symbol);

                if (v.next[symbolIndex] != Node.NA) {
                    // advancing to the next symbol in Text,
                    // and to the next node in trie
                    pattern = pattern.concat(String.valueOf(indexToLetter(symbolIndex)));
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

    boolean isNodeLeaf(Node node) {
        return node.next[0] == Node.NA
                && node.next[1] == Node.NA
                && node.next[2] == Node.NA
                && node.next[3] == Node.NA;
    }

    List<Node> buildTrie(List<String> patterns) {
        List<Node> trie = new ArrayList<>();

        // write your code here
        trie.add(new Node());

        for (String pattern : patterns) {
            int currentNodeIndex = 0;

            for (int i = 0; i < pattern.length(); i++) {
                char currentSymbol = pattern.charAt(i);
                Node currentNode = trie.get(currentNodeIndex);
                int currentSymbolIndex = letterToIndex(currentSymbol);

                if (currentNode.next[currentSymbolIndex] == Node.NA) {
                    Node newNode = new Node();
                    trie.add(newNode);
                    int newNodeIndex = trie.size() - 1;
                    currentNode.next[currentSymbolIndex] = newNodeIndex;
                    currentNodeIndex = newNodeIndex;
                } else {
                    currentNodeIndex = currentNode.next[currentSymbolIndex];
                }
            }
        }

//        print(trie);
        return trie;
    }

    public void print(List<Node> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            Node node = trie.get(i);
            for (int j = 0; j < 4; ++j) {
                int entry = node.next[j];
                if (entry != Node.NA) {
                    System.out.println(i + "->" + entry + ":" + indexToLetter(j));
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
        new Thread(new TrieMatching()).start();
    }
}
