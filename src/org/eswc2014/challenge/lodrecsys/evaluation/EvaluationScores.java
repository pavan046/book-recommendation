/**
 * EvaluationScores.java
 *
 * Stores the training, test (real and predicted) scores/ratings to be used in
 * the evaluation of certain recommendation method.
 *
 * The training scores are read from a plain-text file with the following line
 * format: user_id, item_id, training_score.
 *
 * The test (real and predicted) scores are read from a plain-text file with the
 * following line format: user_id, item_id, real_score, predicted_score.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class EvaluationScores {

    private static final int SCORES_REAL = 1;
    private static final int SCORES_PREDICTED = 2;
    private Map<String, Map<String, Score>> trainingScoreMap;
    private Map<String, Map<String, Score>> realTestScoreMap;
    private Map<String, Map<String, Score>> predictedTestScoreMap;

    /**
     * Builds an EvaluationScores instance storing the training and (real and
     * predicted) test scores read from the files given as input arguments.
     *
     * @param trainingScoreFile the path/name of the file with the training
     * scores to store
     * @param testScoreFile the path/name of the file with the test scores to
     * store
     *
     * @throws IllegalArgumentException in case of null or empty file(s)
     */
    public EvaluationScores(String trainingScoreFile, String testScoreFile) throws Exception {
        if (trainingScoreFile == null) {
            throw new IllegalArgumentException("null training score file name");
        }
        if (testScoreFile == null) {
            throw new IllegalArgumentException("null test score file name");
        }

        List<Score> trainingScores = this.readScoreFile(trainingScoreFile);
        if (trainingScores == null || trainingScores.isEmpty()) {
            throw new IllegalArgumentException("no training scores");
        }

        List<Score> realTestScores = this.readScoreFile(testScoreFile, SCORES_REAL);
        if (realTestScores == null || realTestScores.isEmpty()) {
            throw new IllegalArgumentException("no real test scores");
        }

        List<Score> predictedTestScores = this.readScoreFile(testScoreFile, SCORES_PREDICTED);
        if (predictedTestScores == null || predictedTestScores.isEmpty()) {
            throw new IllegalArgumentException("no predicted test scores");
        }

        this.trainingScoreMap = new HashMap<String, Map<String, Score>>();
        for (int i = 0; i < trainingScores.size(); i++) {
            String userId = trainingScores.get(i).getUserId();
            String itemId = trainingScores.get(i).getItemId();
            Score trainingScore = trainingScores.get(i);

            if (!this.trainingScoreMap.containsKey(userId)) {
                this.trainingScoreMap.put(userId, new HashMap<String, Score>());
            }
            this.trainingScoreMap.get(userId).put(itemId, trainingScore);
        }

        this.realTestScoreMap = new HashMap<String, Map<String, Score>>();
        for (int i = 0; i < realTestScores.size(); i++) {
            String userId = realTestScores.get(i).getUserId();
            String itemId = realTestScores.get(i).getItemId();
            Score trainingScore = realTestScores.get(i);

            if (!this.realTestScoreMap.containsKey(userId)) {
                this.realTestScoreMap.put(userId, new HashMap<String, Score>());
            }
            this.realTestScoreMap.get(userId).put(itemId, trainingScore);
        }

        this.predictedTestScoreMap = new HashMap<String, Map<String, Score>>();
        for (int i = 0; i < predictedTestScores.size(); i++) {
            String userId = predictedTestScores.get(i).getUserId();
            String itemId = predictedTestScores.get(i).getItemId();
            Score trainingScore = predictedTestScores.get(i);

            if (!this.predictedTestScoreMap.containsKey(userId)) {
                this.predictedTestScoreMap.put(userId, new HashMap<String, Score>());
            }
            this.predictedTestScoreMap.get(userId).put(itemId, trainingScore);
        }
    }

    private List<Score> readScoreFile(String file) throws Exception {
        return this.readScoreFile(file, SCORES_REAL);
    }

    private List<Score> readScoreFile(String file, int scoreType) throws Exception {
        if (scoreType != SCORES_REAL && scoreType != SCORES_PREDICTED) {
            throw new IllegalArgumentException("invalid score type");
        }

        ArrayList<Score> scores = new ArrayList<Score>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, ", \t");

            String userId = tokenizer.nextToken();
            String itemId = tokenizer.nextToken();
            double scoreValue = 0;
            if (scoreType == SCORES_REAL) {
                scoreValue = Double.valueOf(tokenizer.nextToken());
            } else if (scoreType == SCORES_PREDICTED) {
                Double.valueOf(tokenizer.nextToken());
                scoreValue = Double.valueOf(tokenizer.nextToken());
            }
            scores.add(new Score(userId, itemId, scoreValue));
        }
        reader.close();

        return scores;
    }

    /**
     * Returns a map with the user-item-score tuples associated to the training
     * scores.
     *
     * @returns the user-item-score tuples in the training set
     */
    public Map<String, Map<String, Score>> getTrainingScoreMap() {
        return this.trainingScoreMap;
    }

    /**
     * Returns a map with the user-item-score tuples associated to the real test
     * scores.
     *
     * @returns the user-item-real_score tuples in the test set
     */
    public Map<String, Map<String, Score>> getRealTestScoreMap() {
        return this.realTestScoreMap;
    }

    /**
     * Returns a map with the user-item-score tuples associated to the training
     * scores.
     *
     * @returns the user-item-predicted_score tuples in the test set
     */
    public Map<String, Map<String, Score>> getPredictedTestScoreMap() {
        return this.predictedTestScoreMap;
    }
}
