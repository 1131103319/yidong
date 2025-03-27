import { createRouter, createWebHistory } from 'vue-router'
// HomeView 组件暂时未使用，如需使用请取消注释
// import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    redirect: '/comparison'
  },
  {
    path: '/comparison',
    name: 'comparison',
    component: () => import('../views/DataComparisonView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router