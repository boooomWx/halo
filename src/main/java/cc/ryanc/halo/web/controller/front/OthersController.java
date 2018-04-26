package cc.ryanc.halo.web.controller.front;

import cc.ryanc.halo.model.domain.Post;
import cc.ryanc.halo.model.dto.HaloConst;
import cc.ryanc.halo.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author : RYAN0UP
 * @version : 1.0
 * @date : 2018/4/26
 */
@Controller
public class OthersController {

    @Autowired
    private PostService postService;

    /**
     * 获取文章rss
     *
     * @return rss
     */
    @GetMapping(value = {"feed","feed.xml","atom.xml"},produces = {MediaType.APPLICATION_ATOM_XML_VALUE,MediaType.APPLICATION_RSS_XML_VALUE})
    @ResponseBody
    public String feed(){
        String rssPosts = HaloConst.OPTIONS.get("rss_posts");
        if(StringUtils.isBlank(rssPosts)){
            rssPosts = "20";
        }
        //获取文章列表并根据时间排序
        Sort sort = new Sort(Sort.Direction.DESC,"postDate");
        Pageable pageable = new PageRequest(0,Integer.parseInt(rssPosts),sort);
        Page<Post> postsPage = postService.findPostByStatus(0,pageable);
        List<Post> posts = postsPage.getContent();
        return postService.buildRss(posts);
    }

    /**
     * 获取sitemap
     *
     * @return sitemap
     */
    @GetMapping(value = {"sitemap","sitemap.xml"},produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String siteMap(){
        //获取文章列表并根据时间排序
        Sort sort = new Sort(Sort.Direction.DESC,"postDate");
        Pageable pageable = new PageRequest(0,999,sort);
        Page<Post> postsPage = postService.findPostByStatus(0,pageable);
        List<Post> posts = postsPage.getContent();
        return postService.buildSiteMap(posts);
    }
}
