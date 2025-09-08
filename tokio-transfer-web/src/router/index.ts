import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const HomeView = () => import('../views/HomeView.vue')
const ScheduleTransferView = () => import('../views/ScheduleTransferView.vue')
const TransfersListView = () => import('../views/TransfersListView.vue')

const routes: RouteRecordRaw[] = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/schedule', name: 'schedule', component: ScheduleTransferView },
  { path: '/transfers', name: 'transfers', component: TransfersListView },
  { path: '/:pathMatch(.*)*', redirect: '/' },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
