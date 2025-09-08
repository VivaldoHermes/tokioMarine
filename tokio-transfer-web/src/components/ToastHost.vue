<script setup lang="ts">
import { useToast } from '../composables/useToast'
const { toasts, remove } = useToast()
</script>

<template>
  <div class="toast-host">
    <TransitionGroup name="toast" tag="div" class="toast-stack">
      <div
        v-for="t in toasts"
        :key="t.id"
        class="toast"
        :data-type="t.type"
        role="status"
        aria-live="polite"
      >
        <span class="dot" />
        <span class="msg">{{ t.message }}</span>
        <button class="close" @click="remove(t.id)" aria-label="Dismiss">×</button>
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.toast-host {
  position: fixed;
  top: 16px;
  right: 16px;
  z-index: 9999;
  pointer-events: none;
}
.toast-stack > .toast {
  pointer-events: auto;
}
.toast-enter-from,
.toast-leave-to {
  opacity: 0;
  transform: translateY(-8px) translateX(8px);
}
.toast-enter-active,
.toast-leave-active {
  transition: all 180ms ease;
}
.toast {
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 8px;
  min-width: 260px;
  max-width: 420px;
  padding: 10px 12px;
  margin-bottom: 8px;
  border-radius: 8px;
  background: #111;
  color: #fff;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.18);
  border: 1px solid rgba(255, 255, 255, 0.06);
}
.toast[data-type='success'] {
  background: #0f5132;
  border-color: #0b3e26;
}
.toast[data-type='error'] {
  background: #842029;
  border-color: #6a1a21;
}
.toast[data-type='info'] {
  background: #1d2a44;
  border-color: #162035;
}
.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: currentColor;
  opacity: 0.75;
}
.toast[data-type='success'] .dot {
  color: #7df2b9;
}
.toast[data-type='error'] .dot {
  color: #ff9aa8;
}
.toast[data-type='info'] .dot {
  color: #a7c7ff;
}
.msg {
  line-height: 1.25;
}
.close {
  appearance: none;
  border: none;
  background: transparent;
  color: #fff;
  opacity: 0.8;
  font-size: 18px;
  line-height: 1;
  padding: 0 4px;
  cursor: pointer;
}
.close:hover {
  opacity: 1;
}
</style>
