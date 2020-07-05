package pl.edu.agh.mwo.kw;

public abstract class RankingPrinter {
    protected final String rankingElement;

    public RankingPrinter(String rankingElement) {
        this.rankingElement = rankingElement;
    }

    protected void printRow(String lp, String keyValue, String value, int maxLength){
        StringBuilder rowRanking = new StringBuilder().append(lp).append("  | ").append(keyValue);
        int spacesCount = (keyValue.length() < maxLength)? maxLength - keyValue.length(): 0;
        for(int i =0; i<= spacesCount; i++) rowRanking.append(" ");
        System.out.println( rowRanking+ " | " + value);
    }
}
