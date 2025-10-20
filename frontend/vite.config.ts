import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src') // 配置 @ 指向 src 目录，方便导入
    }
  },
  server: {
    port: 5173, // 前端启动端口（默认5173，可以改）
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端 Spring Boot 服务地址
        changeOrigin: true,              // 修改请求头中的 origin
        rewrite: (path) => path.replace(/^\/api/, '') // 可选：去掉 /api 前缀
      }
    }
  }
})
