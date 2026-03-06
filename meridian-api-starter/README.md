# Meridian API — Interview Starter

A Spring Boot server for a digital content distribution platform. Publishers create
Articles and distribute them to topic-based Channels.

This project is your starting point. Two domain models are fully implemented end-to-end
(Publisher and Channel). Your job is to implement the Article, Submission, and History
features by following the same patterns already in the codebase.

---

## Getting Started

**Prerequisites:** Java 21, Gradle 8+

```bash
cd meridian-api-starter
./gradlew bootRun
```

The server starts on **http://localhost:8585**.

Interactive API docs: **http://localhost:8585/swagger-ui/index.html**

```bash
./gradlew test
```

---

## Domain Model

```
Publisher ──creates──► Article ──submitted to──► Channel
    │                                                │
    └──── approvedChannelIds (must include) ─────────┘
```

### Publisher (fully implemented)

| Field              | Type            | Notes                                 |
|--------------------|-----------------|---------------------------------------|
| id                 | UUID            |                                       |
| name               | String          |                                       |
| email              | String          | Unique                                |
| status             | PublisherStatus | PENDING → ACTIVE ↔ SUSPENDED          |
| approvedChannelIds | Set\<UUID\>     | Channels this publisher may submit to |
| createdAt          | Date            |                                       |
| modifiedAt         | Date            |                                       |

### Channel (fully implemented)

| Field             | Type          | Notes                              |
|-------------------|---------------|------------------------------------|
| id                | UUID          |                                    |
| name              | String        | Unique                             |
| description       | String        |                                    |
| status            | ChannelStatus | ACTIVE or INACTIVE                 |
| maxCapacity       | int           | Max concurrent active submissions  |
| activeSubmissions | int           | Current count                      |
| createdAt         | Date          |                                    |
| modifiedAt        | Date          |                                    |

---

## Existing API

### Publishers — `/api/publishers`

```bash
curl http://localhost:8585/api/publishers
curl http://localhost:8585/api/publishers/550e8400-e29b-41d4-a716-446655440001

curl -X POST http://localhost:8585/api/publishers \
  -H 'Content-Type: application/json' \
  -d '{"name":"My Publisher","email":"contact@example.com"}'

curl -X POST http://localhost:8585/api/publishers/550e8400-e29b-41d4-a716-446655440003/activate
curl -X POST http://localhost:8585/api/publishers/550e8400-e29b-41d4-a716-446655440001/suspend

curl -X POST http://localhost:8585/api/publishers/550e8400-e29b-41d4-a716-446655440003/approveChannel/550e8400-e29b-41d4-a716-446655440011
curl -X POST http://localhost:8585/api/publishers/550e8400-e29b-41d4-a716-446655440003/revokeChannel/550e8400-e29b-41d4-a716-446655440011
```

### Channels — `/api/channels`

```bash
curl http://localhost:8585/api/channels
curl http://localhost:8585/api/channels/550e8400-e29b-41d4-a716-446655440011

curl -X POST http://localhost:8585/api/channels \
  -H 'Content-Type: application/json' \
  -d '{"name":"Science","description":"Research and discovery","maxCapacity":8}'

curl -X POST http://localhost:8585/api/channels/550e8400-e29b-41d4-a716-446655440013/activate
curl -X POST http://localhost:8585/api/channels/550e8400-e29b-41d4-a716-446655440011/deactivate
```

---

## Pre-Seeded Data

All data resets on every server restart.

| Entity                | ID                                     | Status   | Notes                               |
|-----------------------|----------------------------------------|----------|-------------------------------------|
| Publisher: TechDaily  | `550e8400-e29b-41d4-a716-446655440001` | ACTIVE   | Approved for Technology channel     |
| Publisher: SportsNow  | `550e8400-e29b-41d4-a716-446655440002` | ACTIVE   | Approved for Sports and Technology  |
| Publisher: NewbieBlog | `550e8400-e29b-41d4-a716-446655440003` | PENDING  | No channel approvals                |
| Channel: Technology   | `550e8400-e29b-41d4-a716-446655440011` | ACTIVE   | maxCapacity=10, activeSubmissions=2 |
| Channel: Sports       | `550e8400-e29b-41d4-a716-446655440012` | ACTIVE   | maxCapacity=5, activeSubmissions=3  |
| Channel: Lifestyle    | `550e8400-e29b-41d4-a716-446655440013` | INACTIVE | maxCapacity=8, activeSubmissions=0  |

---

## Your Task

### Part 1 — Article

Publishers need to be able to create articles and move them through an editorial workflow
before they can be distributed. An article starts as a draft, goes through review, and
eventually gets published. Reviewers can also send it back if changes are needed, and
published articles can be archived when they're no longer relevant.

A few things to keep in mind:

- Articles belong to a publisher. That relationship shouldn't be changeable after the fact.
- Not every article status makes sense as a starting point for every transition — enforce that.
- The `publisherId` should be validated on save.
- Think about what fields make sense to include and what their types should be. Look at how
  `Publisher` and `Channel` are modeled for reference.
- Follow the same layered pattern as the existing code — don't shortcut the manager layer.

There's no strict endpoint list — figure out what makes sense based on the workflow described
and what's already in the codebase for the other models.

---

### Part 2 — Submission

Once an article is published, a publisher should be able to submit it to a channel for
distribution. Not every publisher can submit to every channel — there's an approval
mechanism already on the publisher. Channels also have a concept of capacity, and the
system should track how many active submissions a channel has at any time.

Think about what validations need to happen before a submission is accepted, and what
side effects a successful submission should trigger. There are also cases where a
submission should be outright rejected — duplicate submissions being one of them.

The data model and endpoint design are up to you. Look at what's already there and be
consistent with the patterns you see.

---

### Part 3 — History Tracking

The business wants an audit trail: every time an entity's status changes, the system
should record what the status was before, what it changed to, and when it happened.
This should work for all entities that have a status field, not just one.

Once you've implemented it, expose a read-only endpoint so callers can retrieve the
history for a given entity.

---

**Typed exceptions** (already provided):

| Exception                | Suggested HTTP Status |
|--------------------------|-----------------------|
| NotFoundException        | 404                   |
| AlreadyExistsException   | 409                   |
| ValidationException      | 400                   |
| IllegalArgumentException | 400                   |
