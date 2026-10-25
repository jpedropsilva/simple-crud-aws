
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
      name  = "simple-crud"
      image = "750686145863.dkr.ecr.eu-west-3.amazonaws.com/simple-crud:latest"

      essential = true

      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ]

      environment = [
        {
          name = "DB_HOST"
          # value = "your-rds-endpoint.eu-west-3.rds.amazonaws.com"
          value = "${aws_db_instance.postgres.address}"
        },
        {
          name = "DB_PORT"
          value = "5432"
          # value = "mem"
        },
        {
          name  = "DB_NAME"
          value = "simple_crud_db"
          # value = "your_database_name"
        },
        {
          name = "DB_USER"
          value = "simple_crud_user"
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