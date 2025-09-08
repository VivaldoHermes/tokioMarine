import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/HomeView.vue'),
  },
  {
    path: '/schedule',
    name: 'schedule',
    component: () => import('../views/ScheduleTransferView.vue'),
  },
  {
    path: '/transfers',
    name: 'transfers',
    component: () => import('../views/TransfersListView.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
