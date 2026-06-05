
    const modeButtons = document.querySelectorAll("[data-mode]");
    const loginForm = document.querySelector("#loginForm");
    const registerForm = document.querySelector("#registerForm");
    const passwordForm = document.querySelector("#passwordForm");
    const loginButton = document.querySelector("#loginButton");
    const registerButton = document.querySelector("#registerButton");
    const passwordButton = document.querySelector("#passwordButton");
    const loginMessage = document.querySelector("#loginMessage");
    const registerMessage = document.querySelector("#registerMessage");
    const passwordMessage = document.querySelector("#passwordMessage");

    api.get("/api/users/me")
        .then(() => {
            window.location.href = "/students.html"; // 已有登录信息，直接跳转到首页
        })
        .catch(() => {
        });

    function switchMode(mode) {
        loginForm.hidden = mode !== "login";
        registerForm.hidden = mode !== "register";
        passwordForm.hidden = mode !== "password";
        loginForm.classList.toggle("hidden", loginForm.hidden);
        registerForm.classList.toggle("hidden", registerForm.hidden);
        passwordForm.classList.toggle("hidden", passwordForm.hidden);
        loginMessage.textContent = "";
        registerMessage.textContent = "";
        passwordMessage.textContent = "";
        if (mode === "login") {
            loginForm.elements.username.focus();
        }
        if (mode === "register") {
            registerForm.elements.username.focus();
        }
        if (mode === "password") {
            passwordForm.elements.username.focus();
        }
    }

    function setMessage(element, text, success) {
        element.textContent = text;
        element.classList.toggle("success", Boolean(success));
    }

    modeButtons.forEach((button) => {
        button.addEventListener("click", () => switchMode(button.dataset.mode));
    });

    // 登录提交的表单
    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        setMessage(loginMessage, "", false);
        loginButton.disabled = true;
        loginButton.textContent = "登录中";
        try {
            await api.post("/api/users/login", {
                username: loginForm.elements.username.value,
                password: loginForm.elements.password.value
            });
            window.location.href = "/students.html";
        } catch (error) {
            setMessage(loginMessage, error.message, false);
        } finally { // 恢复按钮状态
            loginButton.disabled = false; // 防止重复提交表单
            loginButton.textContent = "登录";
        }
    });

    // 注册提交的表单
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        setMessage(registerMessage, "", false);
        registerButton.disabled = true;
        registerButton.textContent = "注册中";
        try {
            await api.post("/api/users/register", {
                username: registerForm.elements.username.value,
                displayName: registerForm.elements.displayName.value,
                password: registerForm.elements.password.value,
                confirmPassword: registerForm.elements.confirmPassword.value
            });
            const username = registerForm.elements.username.value;
            registerForm.reset();
            loginForm.elements.username.value = username;
            loginForm.elements.password.value = "";
            switchMode("login");
            setMessage(loginMessage, "注册成功，请登录", true);
            loginForm.elements.password.focus();
        } catch (error) {
            setMessage(registerMessage, error.message, false);
        } finally {
            registerButton.disabled = false;
            registerButton.textContent = "注册";
        }
    });

    // 修改密码提交的表单
    passwordForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        setMessage(passwordMessage, "", false);
        passwordButton.disabled = true;
        passwordButton.textContent = "保存中";
        try {
            await api.post("/api/users/password", {
                username: passwordForm.elements.username.value,
                oldPassword: passwordForm.elements.oldPassword.value,
                newPassword: passwordForm.elements.newPassword.value,
                confirmPassword: passwordForm.elements.confirmPassword.value
            });
            const username = passwordForm.elements.username.value;
            passwordForm.reset();
            loginForm.elements.username.value = username;
            loginForm.elements.password.value = "";
            switchMode("login");
            setMessage(loginMessage, "密码修改成功，请重新登录", true);
            loginForm.elements.password.focus();
        } catch (error) {
            setMessage(passwordMessage, error.message, false);
        } finally {
            passwordButton.disabled = false;
            passwordButton.textContent = "保存新密码";
        }
    });
