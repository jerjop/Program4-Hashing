
import java.io.*;
import java.util.Scanner;

public class Reviews {

    public Reviews() {
        H = new QuadraticProbingHashTable<WordInfo>();
    }

    public String toString() {
        int LIMIT = 20;
        return name + "\n" + H.toString(LIMIT);
    }

    private String name;
    private QuadraticProbingHashTable<WordInfo> H;

    public void readReviews(String filename) throws FileNotFoundException, IOException {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String name = null;
            String line;
            String[] words = null;

            int score = -1;
            int line_count = 0;
            while ((line = in.readLine()) != null) {
                line_count++;
                words = line.split("\\s+");
                try {
                    score = Integer.parseInt(words[0]);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Expected integer at line " + line_count + " in file " + filename);
                }
                ReviewInfo r = new ReviewInfo(score, words);
                System.out.println(r.toString());
            }
            System.out.println("Number of Reviews " +  line_count);
    }

    public void learnFromFile() throws FileNotFoundException {
        File file = new File("movieReviews.txt");
        Scanner sc = new Scanner(file);

        boolean isNew = true;
        int rating = 0;

        while (sc.hasNext()) {
            String word = sc.next().toLowerCase();
            if (isNew) {
                rating = Integer.parseInt(word);
                isNew = false;
            } else {
                if (!word.equals(".")) {
                    if (Character.isLetter(word.charAt(0))) {
                        WordInfo info = new WordInfo(word);
                        if(!H.contains(info)) {
                            H.insert(info);
                        } else {
                            info.update(rating);
                        }
                    }
                } else {
                    isNew = true;
                }
            }
        }
    }

    public void handleUserReviewInfo() {
        double sum = 0.0d;
        int count = 0;
        double average = 0.0d;

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a review: ");

        while (sc.hasNext()) {
            String input = sc.next().toLowerCase();
            if(!input.equals(".")) {
                WordInfo info = new WordInfo(input);
                WordInfo entry = H.find(info);
                if(entry != null) {
                    sum += entry.totalScore;
                    count++;
                    average = entry.getAverage();
                }
            } else {
                break;
            }
        }

        System.out.println("The review has an average value of " + average);

        if (average < 1.75) {
            System.out.println("Negative");
        } else if (average >= 1.75 && average < 2.25) {
            System.out.println("Neutral");
        } else {
            System.out.println("Positive");
        }
    }

    private static class ReviewInfo {
        int score;
        String[] words;

        // Constructors
        ReviewInfo(int score, String[] words) {
            this.score = score;
            this.words = words;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Review " + score+ " Length of Review " + (words.length -1) + " ");
            for (int i = 1; i < 11 & i < words.length; i++)
                sb.append(words[i] + " ");
            return sb.toString();
        }
    }

    private static class WordInfo {
        int totalScore;
        int numberOfOccurences;
        String word;

        // Constructors
        WordInfo(String word) {
            this.word = word;
            totalScore=0;
            numberOfOccurences = 0;
        }

        public double getAverage() {
            double total = totalScore;
            double count = numberOfOccurences;
            return total / count;
        }

        public void update(int score){
            this.totalScore+=score;
            this.numberOfOccurences++;
        }

        public String toString() {
           return "Word " + word + " [" + totalScore +", " + numberOfOccurences+"]";
        }
    }

        public static void main (String[ ]args ){
//            try {
//                Reviews r1 = new Reviews();
//                r1.readReviews("movieReviews.txt");
//                System.out.println(r1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            WordInfo w = new WordInfo("fat");
//            w.update(4);
//            System.out.println(w.toString());

            try {
                Reviews r1 = new Reviews();
                r1.learnFromFile();
                r1.handleUserReviewInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
