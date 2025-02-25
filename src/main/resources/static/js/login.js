document
    .getElementById("loginForm")
    .addEventListener(
        "submit",
        function (event) {
            event.preventDefault()
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            let requestData = {
                email: email,
                password: password
            };

            $.ajax({
                url: "/api/login",
                type: "POST",
                data: JSON.stringify(requestData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                timeout: 30000,
                async: true,
                success: function (data) {
                    window.location.href = "/dashboard";
                    // alert("login success 123");
                },
                error: function (xhr, textStatus,
                                 errorThrown) {
                    console.error("Error occurred during authentication");
                },
                complete: function () {
                    console.log("Request completed");
                    // document.getElementById("loadingSpinner").style.display = "none";
                    // $(".btn-dark").prop("disabled",false);
                }
            });
        });