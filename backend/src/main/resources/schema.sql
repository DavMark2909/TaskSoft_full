DROP TABLE IF EXISTS user_groups;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS user_group_membership;

-- Create custom ENUM types first, as tables will depend on them
CREATE TYPE task_type_enum AS ENUM (
    'CREATED',
    'IN_PROGRESS',
    'COMPLETED',
    'FAILED',
    'DELAYED'
);

CREATE TYPE notification_type_enum AS ENUM (
    'SINGLE',
    'GROUP'
);

---
-- Table for Users
-- (Depends on: nothing)
---
CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   username VARCHAR(255) UNIQUE NOT NULL,
   first_name VARCHAR(255),
   last_name VARCHAR(255)
);

CREATE TABLE user_groups (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255) NOT NULL
);

CREATE TABLE task (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  task_type task_type_enum NOT NULL,

-- Foreign Keys
  assigner_id BIGINT NOT NULL,
  assignee_user_id BIGINT,
  assignee_group_id BIGINT,

-- Constraints
  CONSTRAINT fk_task_assigner
      FOREIGN KEY (assigner_id)
          REFERENCES users(id)
          ON DELETE CASCADE,

  CONSTRAINT fk_task_assignee_user
      FOREIGN KEY (assignee_user_id)
          REFERENCES users(id)
          ON DELETE SET NULL, -- If user is deleted, task becomes unassigned

  CONSTRAINT fk_task_assignee_group
      FOREIGN KEY (assignee_group_id)
          REFERENCES user_groups(id)
          ON DELETE SET NULL -- If group is deleted, task becomes unassigned
);

CREATE TABLE notifications (
   id BIGSERIAL PRIMARY KEY,
   message TEXT NOT NULL,
   is_read BOOLEAN DEFAULT FALSE,
   notification_type notification_type_enum NOT NULL,

   recipient_id BIGINT NOT NULL,
   task_id BIGINT NOT NULL,

   CONSTRAINT fk_notification_recipient
       FOREIGN KEY (recipient_id)
           REFERENCES users(id)
           ON DELETE CASCADE, -- If user is deleted, delete their notifications

   CONSTRAINT fk_notification_task
       FOREIGN KEY (task_id)
           REFERENCES task(id)
           ON DELETE CASCADE -- If task is deleted, delete notifications for it
);

CREATE TABLE user_group_membership (
   group_id BIGINT NOT NULL,
   user_id BIGINT NOT NULL,

   PRIMARY KEY (group_id, user_id), -- Composite primary key

   CONSTRAINT fk_membership_group
       FOREIGN KEY (group_id)
           REFERENCES user_groups(id)
           ON DELETE CASCADE,

   CONSTRAINT fk_membership_user
       FOREIGN KEY (user_id)
           REFERENCES users(id)
           ON DELETE CASCADE
);

CREATE INDEX idx_task_assigner_id ON task(assigner_id);
CREATE INDEX idx_task_assignee_user_id ON task(assignee_user_id);
CREATE INDEX idx_task_assignee_group_id ON task(assignee_group_id);

CREATE INDEX idx_notifications_recipient_id ON notifications(recipient_id);
CREATE INDEX idx_notifications_task_id ON notifications(task_id);

CREATE INDEX idx_membership_group_id ON user_group_membership(group_id);
CREATE INDEX idx_membership_user_id ON user_group_membership(user_id);