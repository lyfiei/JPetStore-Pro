#!/bin/bash
# ============================================
# JPetStore 一键部署脚本
# 用途：拉取代码 + 构建镜像 + 启动容器
# 使用：bash deploy.sh
# ============================================

set -e

echo "=============================="
echo "  JPetStore Docker 一键部署"
echo "=============================="

# ---------- 配置 ----------
JPETSTORE_DIR="/data/workspace/JPetStore-Pro"
ADMIN_DIR="/data/workspace/JPetstore_Admin"
JPETSTORE_REPO="https://github.com/lyfiei/JPetStore-Pro.git"
ADMIN_REPO="https://github.com/lyfiei/JPetstore_Admin.git"

# ---------- 检测 Docker Compose 命令 ----------
if docker compose version > /dev/null 2>&1; then
    DC="docker compose"
elif docker-compose version > /dev/null 2>&1; then
    DC="docker-compose"
else
    echo "[ERROR] 未找到 docker compose，请安装 Docker Compose 插件"
    exit 1
fi
echo "[OK] 使用: $DC"

# ---------- 1. 检查 Docker ----------
if ! docker info > /dev/null 2>&1; then
    echo "[ERROR] Docker 未运行，请先执行: sudo systemctl start docker"
    exit 1
fi
echo "[OK] Docker 运行中"

# ---------- 2. 克隆前台项目 ----------
if [ ! -d "$JPETSTORE_DIR" ]; then
    echo "[STEP] 克隆前台项目: $JPETSTORE_REPO"
    mkdir -p "$(dirname "$JPETSTORE_DIR")"
    git clone "$JPETSTORE_REPO" "$JPETSTORE_DIR"
else
    echo "[OK] 前台项目已存在: $JPETSTORE_DIR"
    cd "$JPETSTORE_DIR" && git pull && cd - > /dev/null
fi

# ---------- 3. 克隆后台管理项目 ----------
if [ ! -d "$ADMIN_DIR" ]; then
    echo "[STEP] 克隆后台管理项目: $ADMIN_REPO"
    mkdir -p "$(dirname "$ADMIN_DIR")"
    git clone "$ADMIN_REPO" "$ADMIN_DIR"
else
    echo "[OK] 后台管理项目已存在: $ADMIN_DIR"
    cd "$ADMIN_DIR" && git pull && cd - > /dev/null
fi

# ---------- 4. 进入前台项目，构建并启动 ----------
cd "$JPETSTORE_DIR"

echo ""
echo "[STEP] 构建镜像并启动所有容器..."
$DC up -d --build

# ---------- 5. 等待服务就绪 ----------
echo ""
echo "[STEP] 等待 MySQL 就绪..."
sleep 15

# ---------- 6. 运行状态 ----------
echo ""
echo "[STEP] 容器运行状态:"
$DC ps

echo ""
echo "=============================="
echo "  部署完成！"
echo "  前台访问: http://localhost"
echo "  后台管理: http://localhost:81"
echo "  查看日志: $DC logs -f"
echo "=============================="
