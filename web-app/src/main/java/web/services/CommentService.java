package web.services;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.entities.Comment;
import web.entities.Post;
import web.repositories.CommentRepository;
import web.repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
@AllArgsConstructor
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public void updateCommentPrediction() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        List<Post> posts = postRepository.findAll();
        for (Post p:
             posts) {
           List<Comment> comments = commentRepository.findByPostId(p.getId());
           List<String> contents = new ArrayList<>();
           comments.forEach(c -> contents.add(c.getContent()));
           String text = String.join(". ", contents);
           Annotation annotation = pipeline.process(text);
            int mainSentiment = 0;
            int longest = 0;
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }
            }
            System.out.println("aaa "+ mainSentiment);
            if(mainSentiment < 2) {
                p.setPrediction("negative");
            } else if(mainSentiment > 2) {
                p.setPrediction("positive");
            } else {
                p.setPrediction("neutral");
            }
            System.out.println("Handle post " + p.getCode());
            postRepository.save(p);
        }
//        List<Comment> comments = commentRepository.findAll();
//        for (Comment cmt:
//             comments) {
//
//            Annotation annotation = pipeline.process(cmt.getContent());
//            int mainSentiment = 0;
//            int longest = 0;
//            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
//                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
//                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
//                String partText = sentence.toString();
//                if (partText.length() > longest) {
//                    mainSentiment = sentiment;
//                    longest = partText.length();
//                }
//            }
//            System.out.println("aaa "+ mainSentiment);
//            if(mainSentiment < 2) {
//                cmt.setPrediction("negative");
//            } else if(mainSentiment > 2) {
//                cmt.setPrediction("positive");
//            } else {
//                cmt.setPrediction("neutral");
//            }
//            System.out.println("Handle comment " + cmt);
//            commentRepository.save(cmt);
//        }
    }


}
