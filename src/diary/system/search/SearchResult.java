package diary.system.search;

/**
 * 検索結果の格納
 * @author Masato Suzuki
 */
public class SearchResult {
	/**
	 * 日記データ
	 */
	private SearchData[] searchData;

	/**
	 * ソートインデックス
	 */
	private int[][] sortIndex;

	/**
	 * コンストラクタ
	 * @param searchData 日記データ
	 * @param sortIndex ソートインデックス
	 */
	public SearchResult(SearchData[] searchData, int[][] sortIndex) {
		this.searchData = searchData;
		this.sortIndex = sortIndex;
	}

	/**
	 * 検索結果（日記データ）を返す
	 * @return 日記データ
	 */
	public SearchData[] getSearchData(){
		return searchData;
	}

	/**
	 * 検索結果（ソートインデックス）を返す
	 * @return ソートインデックス
	 */
	public int[][] getsortIndex() {
		return sortIndex;
	}
}
