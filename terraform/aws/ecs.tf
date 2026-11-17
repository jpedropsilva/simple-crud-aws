resource "aws_cloudwatch_log_group" "ecs" {
  name              = "/ecs/simple-crud"
  retention_in_days = 7
}
resource "aws_ecs_cluster" "main" {
  name = "simple-crud-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "simple-crud-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]

  cpu    = 256
  memory = 512

  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name = "simple-crud"
      # Only the initial image. After the first apply, GitHub Actions registers new
      # revisions with the image it built (see lifecycle block in service.tf).
      image = var.initial_container_image

      essential = true

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.ecs.name
          "awslogs-region"        = "eu-west-3"
          "awslogs-stream-prefix" = "ecs"
        }
      }

      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        },
        {
          # Spring Boot actuator (management port), used only by the ALB health check
          containerPort = 9090
          hostPort      = 9090
        }
      ]

      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "deploy"
        },
        {
          name = "DB_HOST"
          # value = "your-rds-endpoint.eu-west-3.rds.amazonaws.com"
          value = "${aws_db_instance.postgres.address}"
        },
        {
          name  = "DB_PORT"
          value = "5432"
          # value = "mem"
        },
        {
          name  = "DB_NAME"
          value = "simple_crud_db"
          # value = "your_database_name"
        },
        {
          name  = "DB_USER"
          value = var.db_username
          # value = "sa"
        }
      ]

      ## Best Practice: Use ECS secrets for sensitive data like passwords
      secrets = [
        {
          name      = "DB_PASSWORD"
          valueFrom = "arn:aws:secretsmanager:eu-west-3:750686145863:secret:/dev/db/simpleCrud-wsX3OC:password::"
        }
      ]
    }
  ])
}