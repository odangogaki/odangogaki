package wordbooktyping;


//何か実装するときのテスト用クラス、使ってはない
public class Test {

	public static void main(String[]args){

		WordList wordlist = new WordList();
		WordList word = wordlist.wordset();
		String strja = word.japanesecodearray[0];
		String strea = word.englishcodearray[10];

		String strjw = word.japanesewordlist.get(0);
		String strew = word.englishwordlist.get(10);


		System.out.println(strja +" = "+ strjw);
		System.out.println(strea +" = "+ strew);

	}

}
