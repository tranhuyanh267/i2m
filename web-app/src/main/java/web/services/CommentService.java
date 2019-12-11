package web.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.dtos.CommentDto;
import web.entities.Comment;
import web.entities.Post;
import web.repositories.CommentRepository;
import web.repositories.PostRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

//    public void updateCommentPrediction() {
//        Properties props = new Properties();
//        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        List<Post> posts = postRepository.findByPrediction(null);
//        for (Post p:
//             posts) {
//           List<Comment> comments = commentRepository.findByPostId(p.getId());
//           List<String> contents = new ArrayList<>();
//           comments.forEach(c -> contents.add(c.getContent()));
//           String text = String.join(". ", contents);
//           Annotation annotation = pipeline.process(text);
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
//                p.setPrediction("negative");
//            } else if(mainSentiment > 2) {
//                p.setPrediction("positive");
//            } else {
//                p.setPrediction("neutral");
//            }
//            System.out.println("Handle post " + p.getCode());
//            postRepository.save(p);
//        }
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
//    }

    public void writeCommentToJson() throws IOException {
        JSONObject arrayData = new JSONObject();
        List<Post> posts = postRepository.findAll();
        int count = 0;
        for (Post p:
             posts) {
            count++;
            List<Comment> comments = commentRepository.findByPostId(p.getId());
            List<String> contents = new ArrayList<>();
            comments.forEach(c -> contents.add(c.getContent()));
            String text = String.join(". ", contents);
            System.out.println("count " + count);
            JSONObject sampleObject = new JSONObject();
            sampleObject.put("id", p.getId());
            sampleObject.put("comment", text);
            arrayData.put(p.getId(), sampleObject);
        }

        Files.write(Paths.get("classified_comments.json"), arrayData.toJSONString().getBytes());
    }

    public void readFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<CommentDto>> typeReference = new TypeReference<List<CommentDto>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/comment.json");
        try {
            List<CommentDto> comments = mapper.readValue(inputStream,typeReference);
            comments.forEach((c) -> {
                Post p = postRepository.findById(c.getId()).orElse(null);
                p.setPrediction(c.getPrediction());
                postRepository.save(p);
            });
            System.out.println("Comments Saved!");
        } catch (IOException e){
            System.out.println("Unable to save comment: " + e.getMessage());
        }
    }
}
