package com.apple.meridian;

import com.apple.meridian.manager.ChannelManager;
import com.apple.meridian.manager.PublisherManager;
// import com.apple.meridian.manager.ArticleManager;
import com.apple.meridian.model.Channel;
import com.apple.meridian.model.ChannelStatus;
// import com.apple.meridian.model.Article;
// import com.apple.meridian.model.ArticleStatus;
import com.apple.meridian.model.Publisher;
import com.apple.meridian.model.PublisherStatus;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
// import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Seeds the in-memory store with deterministic data on startup.
 *
 * <p>All IDs use the pattern {@code 550e8400-e29b-41d4-a716-4466554400XX}
 * so they can be referenced in curl commands and README examples without
 * looking them up first.</p>
 *
 * <p>The seeded data is designed to cover a range of states, making it easy to
 * demonstrate business rules without creating additional records:</p>
 * <ul>
 *   <li>TechDaily is ACTIVE and approved only for the Technology channel</li>
 *   <li>SportsNow is ACTIVE and approved for both Sports and Technology</li>
 *   <li>NewbieBlog is PENDING with no channel approvals (useful for testing rejection cases)</li>
 *   <li>The Lifestyle channel is INACTIVE (useful for testing channel validation)</li>
 * </ul>
 */
@Component
public class DataInitializer {

    // Publisher IDs
    public static final UUID TECH_DAILY_ID  = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    public static final UUID SPORTS_NOW_ID  = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
    public static final UUID NEWBIE_BLOG_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");

    // Channel IDs
    public static final UUID TECHNOLOGY_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");
    public static final UUID SPORTS_ID     = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
    public static final UUID LIFESTYLE_ID  = UUID.fromString("550e8400-e29b-41d4-a716-446655440013");

    // Article IDs — uncomment when you implement Article
    // public static final UUID AI_REVOLUTION_ID  = UUID.fromString("550e8400-e29b-41d4-a716-446655440021");
    // public static final UUID BEST_JS_2024_ID   = UUID.fromString("550e8400-e29b-41d4-a716-446655440022");
    // public static final UUID WORLD_CUP_ID      = UUID.fromString("550e8400-e29b-41d4-a716-446655440023");
    // public static final UUID HEALTHY_EATING_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440024");
    // public static final UUID TECH_HISTORY_ID   = UUID.fromString("550e8400-e29b-41d4-a716-446655440025");

    private final PublisherManager publisherManager;
    private final ChannelManager channelManager;
    // private final ArticleManager articleManager;

    public DataInitializer(PublisherManager publisherManager, ChannelManager channelManager) {
        this.publisherManager = publisherManager;
        this.channelManager = channelManager;
    }

    @PostConstruct
    public void seed() {
        seedChannels();
        seedPublishers();
        // seedArticles();  // uncomment when you implement Article
    }

    private void seedChannels() {
        Channel technology = new Channel();
        technology.setId(TECHNOLOGY_ID);
        technology.setName("Technology");
        technology.setDescription("Software, AI, hardware, and emerging tech");
        technology.setStatus(ChannelStatus.ACTIVE);
        technology.setMaxCapacity(10);
        technology.setActiveSubmissions(2);
        technology.setCreatedAt(daysAgo(90));
        technology.setModifiedAt(daysAgo(1));
        channelManager.save(technology);

        Channel sports = new Channel();
        sports.setId(SPORTS_ID);
        sports.setName("Sports");
        sports.setDescription("Game coverage, analysis, and athlete profiles");
        sports.setStatus(ChannelStatus.ACTIVE);
        sports.setMaxCapacity(5);
        sports.setActiveSubmissions(3);
        sports.setCreatedAt(daysAgo(90));
        sports.setModifiedAt(daysAgo(2));
        channelManager.save(sports);

        Channel lifestyle = new Channel();
        lifestyle.setId(LIFESTYLE_ID);
        lifestyle.setName("Lifestyle");
        lifestyle.setDescription("Health, wellness, travel, and culture");
        lifestyle.setStatus(ChannelStatus.INACTIVE);
        lifestyle.setMaxCapacity(8);
        lifestyle.setActiveSubmissions(0);
        lifestyle.setCreatedAt(daysAgo(90));
        lifestyle.setModifiedAt(daysAgo(30));
        channelManager.save(lifestyle);
    }

    private void seedPublishers() {
        // TechDaily: active, approved for Technology only
        Publisher techDaily = new Publisher();
        techDaily.setId(TECH_DAILY_ID);
        techDaily.setName("TechDaily");
        techDaily.setEmail("editor@techdaily.com");
        techDaily.setStatus(PublisherStatus.ACTIVE);
        techDaily.setApprovedChannelIds(new HashSet<>(Set.of(TECHNOLOGY_ID)));
        techDaily.setCreatedAt(daysAgo(60));
        techDaily.setModifiedAt(daysAgo(10));
        publisherManager.save(techDaily);

        // SportsNow: active, approved for Sports and Technology
        Publisher sportsNow = new Publisher();
        sportsNow.setId(SPORTS_NOW_ID);
        sportsNow.setName("SportsNow");
        sportsNow.setEmail("desk@sportsnow.com");
        sportsNow.setStatus(PublisherStatus.ACTIVE);
        sportsNow.setApprovedChannelIds(new HashSet<>(Set.of(SPORTS_ID, TECHNOLOGY_ID)));
        sportsNow.setCreatedAt(daysAgo(45));
        sportsNow.setModifiedAt(daysAgo(5));
        publisherManager.save(sportsNow);

        // NewbieBlog: still pending review, not approved for any channel
        Publisher newbieBlog = new Publisher();
        newbieBlog.setId(NEWBIE_BLOG_ID);
        newbieBlog.setName("NewbieBlog");
        newbieBlog.setEmail("hello@newbieblog.io");
        newbieBlog.setStatus(PublisherStatus.PENDING);
        newbieBlog.setApprovedChannelIds(new HashSet<>());
        newbieBlog.setCreatedAt(daysAgo(3));
        newbieBlog.setModifiedAt(daysAgo(3));
        publisherManager.save(newbieBlog);
    }

    private static Date daysAgo(int days) {
        return Date.from(Instant.now().minus(days, ChronoUnit.DAYS));
    }

    // private void seedArticles() {
    //     // TODO: seed articles here once you implement the Article model
    // }
}
