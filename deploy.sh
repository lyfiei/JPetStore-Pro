#!/bin/bash
# ============================================
# JPetStore 一键部署脚本
# 用途：构建镜像 + 启动所有容器
# 使用：bash deploy.sh
# ============================================

set -e

echo "=============================="
echo "  JPetStore Docker 部署脚本"
echo "=============================="

# 1. 检查 Docker 是否运行
if ! docker info > /dev/null 2>&1; then
    echo "[ERROR] Docker 未运行，请先执行: sudo systemctl start docker"
    exit 1
fi
echo "[OK] Docker 运行中"

# 2. 构建并启动所有容器
echo ""
echo "[STEP] 构建镜像并启动容器..."
docker-compose up -d --build

# 3. 等待服务就绪
echo ""
echo "[STEP] 等待 MySQL 就绪..."
sleep 15

# 4. 显示运行状态
echo ""
echo "[STEP] 容器运行状态:"
docker-compose ps

echo ""
echo "=============================="
echo "  部署完成！"
echo "  前台访问: http://localhost"
echo "  后台管理: http://localhost:81"
echo "  查看日志: docker-compose logs -f"
echo "=============================="
