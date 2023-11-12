provider "aws" {
  region = "us-east-1" # Replace with your desired AWS region
}


#Create IAM role for Lambda function
resource "aws_iam_role" "lambda_tt_execution_role" {
  name = "lambda_tt_execution_role"
  
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
  
  tags = {
    user = "terraform"
  }
}


# Attach policies to Lambda role (e.g., AWSLambdaBasicExecutionRole for basic Lambda execution permissions)
resource "aws_iam_role_policy_attachment" "lambda_policy_attachment" {
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
  role       = aws_iam_role.lambda_tt_execution_role.name
}


# Create Lambda function
resource "aws_lambda_function" "fasal_fusion_function" {
  function_name    = "fasal_fusion_function"
  role             = aws_iam_role.lambda_tt_execution_role.arn  # Use the ARN, not the name
  handler          = "com.example.MyLambdaFunction::handleRequest"
  runtime          = "java11"
  filename         = "C:\\Users\\Manav Khandurie\\Downloads\\FASAL-FUSION\\webApp\\Backend-Lambda\\target\\MyLambdaFunction-1.0.0.jar"
  tags = {
    user = "terraform"
  }
}



# Create API Gateway
resource "aws_api_gateway_rest_api" "fasal_fusion_gateway" {
  name        = "fasal_fusion_gateway"
  description = "Fasal Fusion API Gateway"
}

# Create API Gateway resource
resource "aws_api_gateway_resource" "fasal_fusion_gateway_resource" {
  rest_api_id = aws_api_gateway_rest_api.fasal_fusion_gateway.id
  parent_id   = aws_api_gateway_rest_api.fasal_fusion_gateway.root_resource_id
  path_part   = "calc"
}

# Create API Gateway method
resource "aws_api_gateway_method" "fasal_fusion_gateway_method" {
  rest_api_id   = aws_api_gateway_rest_api.fasal_fusion_gateway.id
  resource_id   = aws_api_gateway_resource.fasal_fusion_gateway_resource.id
  http_method   = "POST"
  authorization = "NONE"
}

# Enable CORS for API Gateway resource
resource "aws_api_gateway_method" "cors" {
  rest_api_id   = aws_api_gateway_rest_api.fasal_fusion_gateway.id
  resource_id   = aws_api_gateway_resource.fasal_fusion_gateway_resource.id
  http_method   = "OPTIONS"
  authorization = "NONE"
}

# Link Lambda function to API Gateway
resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id             = aws_api_gateway_rest_api.fasal_fusion_gateway.id
  resource_id             = aws_api_gateway_resource.fasal_fusion_gateway_resource.id
  http_method             = aws_api_gateway_method.fasal_fusion_gateway_method.http_method
  integration_http_method = "POST"
  type                    = "AWS_PROXY"
  uri                     = aws_lambda_function.fasal_fusion_function.invoke_arn
}

# # Create API Gateway deployment
# resource "aws_api_gateway_deployment" "deployment" {
#   depends_on = [aws_api_gateway_integration.lambda_integration]
#   rest_api_id = aws_api_gateway_rest_api.fasal_fusion_gateway.id
#   stage_name  = "prod"
# }

# output "api_gateway_url" {
#   value = aws_api_gateway_deployment.deployment.invoke_url
# }
