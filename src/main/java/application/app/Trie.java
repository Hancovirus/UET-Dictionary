package application.app;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Trie {
    private HashMap<Character, Trie> mp;
    private String value;
    private boolean added = false;

    public Trie() {
        this(null);
    }

    private Trie(String value) {
        this.value = value;
        mp = new HashMap<Character, Trie>();
    }

    private void addNode(char c) {
        String s;
        if (this.value == null) {
            s = Character.toString(c);
        }
        else s = this.value + Character.toString(c);
        mp.put(c, new Trie(s));
    }

    public void insert(String insertWord) {
        if (insertWord == null) {
            System.out.println("Điền đê");
            return;
        }
        Trie trieNode = this;
        for (char c : insertWord.toCharArray()) {
            if (!trieNode.mp.containsKey(c)) {
                trieNode.addNode(c);
            }
            trieNode = trieNode.mp.get(c);
        }
        trieNode.added = true;
    }

    public ObservableList<String> autoComplete(String insertWord) {
        Trie trieNode = this;
        for (char c : insertWord.toCharArray()) {
            if (!trieNode.mp.containsKey(c)) {
                return null;
            }
            trieNode = trieNode.mp.get(c);
        }
        return trieNode.allComplete();
    }

    private ObservableList<String> allComplete() {
        ObservableList<String> completeArr = FXCollections.observableArrayList();
        if (this.added) {
            completeArr.add(this.value);
        }
        for (Map.Entry<Character, Trie> set : mp.entrySet()) {
            Trie trienode = set.getValue();
            ObservableList<String> arr = trienode.allComplete();
            completeArr.addAll(arr);
        }
        return completeArr;
    }

    public void delete(String deleteWord) {
        Trie trieNode = this;
        for (char c : deleteWord.toCharArray()) {
            if (!trieNode.mp.containsKey(c)) {
                return;
            }
            trieNode = trieNode.mp.get(c);
        }
        trieNode.added = false;
    }
}

