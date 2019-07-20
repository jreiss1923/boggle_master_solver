class Trie{

    TrieNode root;

    Trie(){
        this.root = new TrieNode();
    }

    void insert(String word){
        TrieNode temp = root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            int pos = c - 'a';
            if(temp.arr[pos] == null){
                TrieNode newTemp = new TrieNode();
                temp.arr[pos] = newTemp;
                temp = newTemp;
            }
            else{
                temp = temp.arr[pos];
            }
        }
        temp.isFinal = true;
    }

    boolean search(String word){
        TrieNode temp = this.searchNode(word);
        if(temp == null){
            return false;
        }
        else if(temp.isFinal){
            return true;
        }
        return false;
    }

    TrieNode searchNode(String word){
        TrieNode temp = this.root;
        for(int i = 0; i < word.length(); i++){
            char c = word.charAt(i);
            int pos = c - 'a';
            if(temp.arr[pos] == null){
                return null;
            }
            else{
                temp = temp.arr[pos];
            }
        }

        if(temp == root){
            return null;
        }
        return temp;
    }

}

class TrieNode{
    TrieNode[] arr;
    boolean isFinal;

    TrieNode(){
        this.arr = new TrieNode[26];
    }

}
