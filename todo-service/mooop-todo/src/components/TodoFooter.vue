<script setup>
</script>
<template>
    <footer class="app-footer">

        <!-- 1 Line -->
        <div class="footer-line footer-top">
            <div class="footer-logo">M</div>
            <div class="footer-app-name">MOoop Todo</div>
        </div>

        <!-- 2 Line -->
        <div class="footer-line footer-address">
            서울특별시 강남구 테헤란로 123, MOoop빌딩 8층
        </div>

        <!-- 3 Line -->
        <div class="footer-line footer-contact">
            <span>Tel. 02-1234-5678</span>
            <span class="divider">|</span>
            <span>Email. support@mooop-todo.com</span>
        </div>

    </footer>
</template>
<style scoped>
.app-footer {
    background: #ffffff;
    padding: 24px 16px;
    border-top: 1px solid #e0e0e0;
    box-shadow: 0 -2px 6px rgba(0,0,0,0.04);
    text-align: center;
}

/* 공통 라인 */
.footer-line {
    margin-bottom: 10px;
}

/* 1라인 */
.footer-top {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
}

.footer-logo {
    width: 36px;
    height: 36px;
    background: #1976d2;
    color: #ffffff;
    font-weight: 700;
    font-size: 18px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.footer-app-name {
    font-size: 18px;
    font-weight: 700;
    color: #212121;
}

/* 2라인 */
.footer-address {
    font-size: 13px;
    color: #757575;
}

/* 3라인 */
.footer-contact {
    font-size: 13px;
    color: #616161;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
    flex-wrap: wrap;
}

.footer-contact .divider {
    color: #bdbdbd;
}
</style>