let payButton = document.getElementById("payButton");
if (payButton) {

    payButton.addEventListener(
        "click",
        function () {
            event.preventDefault();
            document.getElementById("loadingSpinner").style.display = "block";

            let paymentAlert = document.getElementById("paymentAlert");
            paymentAlert.style.display = "none";
            paymentAlert.innerHTML = "";
            paymentAlert.className = "alert";

            let payButton = document.getElementById("payButton");
            payButton.disabled = true;


            let fullname = document.getElementById("fullname").value;
            let bill_email = document
                .getElementById("bill_email").value;
            let bill_phone = document
                .getElementById("bill_phone").value;
            let bill_address = document
                .getElementById("bill_address").value;
            let bill_city = document
                .getElementById("bill_city").value;
            let bill_zip = document.getElementById("bill_zip").value;
            let ccno = document.getElementById("ccno").value;
            let month = document.getElementById("month").value;
            let year = document.getElementById("year").value;
            let ccvv = document.getElementById("ccvv").value;
            let bill_amt = document.getElementById("bill_amt").value;

            let notes = document.getElementById("notes").value;

            let requestData = {
                fullname: fullname,
                bill_email: bill_email,
                bill_phone: bill_phone,
                bill_address: bill_address,
                bill_city: bill_city,
                bill_zip: bill_zip,
                ccno: ccno,
                month: month,
                year: year,
                ccvv: ccvv,
                bill_amt: bill_amt,
                notes: notes
            };
            $
                .ajax({
                    url: "/api/payment/checkout",
                    type: "POST",
                    data: JSON.stringify(requestData),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    timeout: 30000,
                    async: true,
                    success: function (data) {
                        showPaymentResult(data.status !== 'error', data.message);
                    },
                    error: function (xhr, textStatus,
                        errorThrown) {
                        alert("The server is busy. Please try again later")
                        console
                            .error("The server is busy. Please try again later");
                        popup.close();
                    },
                    complete: function () {
                        payButton.disabled = false;
                        document.getElementById("loadingSpinner").style.display = "none";
                    }
                });

        });
}



function showPaymentResult(success, message) {
    const alertBox = document.getElementById("paymentAlert");

    if (success) {

        alertBox.className = "alert alert-success";
        alertBox.innerHTML = "Payment success!";
        resetForm();
    } else {
        alertBox.className = "alert alert-danger";
        alertBox.innerHTML = "Payment failed: " + message;
    }

    alertBox.style.display = "block";
}


function resetForm() {
    document.getElementById("ccno").value = "";
    document.getElementById("month").value = "";
    document.getElementById("year").value = "";
    document.getElementById("ccvv").value = "";
    document.getElementById("bill_amt").value = "";
    document.getElementById("notes").value = "";
}


function formatCreditCard(input) {
    let value = input.value.replace(/\D/g, '');
    if (value.length > 16) {
        value = value.substring(0, 16);
    }
    let formattedValue = value.replace(/(\d{4})/g, '$1 ').trim();
    input.value = formattedValue;
}

function inputNumber(input) {
    input.value = input.value.replace(/\D/g, "");

}

