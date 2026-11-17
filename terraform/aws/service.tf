resource "aws_ecs_service" "app" {
  name            = "simple-crud-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn

  desired_count = 1

  launch_type = "FARGATE"

  # Spring Boot needs time to start before the ALB health check counts failures.
  health_check_grace_period_seconds = 90

  # GitHub Actions owns which task definition revision (i.e. which image) is running.
  # Without this, every `terraform apply` would roll the service back to the
  # image defined in ecs.tf, undoing the last CI deploy.
  lifecycle {
    ignore_changes = [
      task_definition
    ]
  }

  network_configuration {
    subnets = [
      aws_subnet.public_a.id,
      aws_subnet.public_b.id
    ]

    security_groups = [
      aws_security_group.web.id
    ]

    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app.arn

    container_name = "simple-crud"
    container_port = 8080
  }

  depends_on = [
    aws_lb_listener.http
  ]
}
