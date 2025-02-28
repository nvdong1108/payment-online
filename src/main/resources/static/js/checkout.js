document
    .getElementById("payButton")
    .addEventListener(
        "click",
        function() {
            event.preventDefault();
            document.getElementById("loadingSpinner").style.display = "block";
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

            let requestData = {
                fullname : fullname,
                bill_email : bill_email,
                bill_phone : bill_phone,
                bill_address : bill_address,
                bill_city : bill_city,
                bill_zip : bill_zip,
                ccno : ccno,
                month : month,
                year : year,
                ccvv : ccvv,
                bill_amt : bill_amt
            };
            $
                .ajax({
                    url : "/api/payment/checkout",
                    type : "POST",
                    data : JSON.stringify(requestData),
                    contentType : "application/json; charset=utf-8",
                    dataType : "json",
                    timeout : 30000,
                    async : true,
                    success : function(data) {
                        if(data.status == 'error'){
                            alert("Payment error: "+data.message);
                        }else {
                            alert("Payment success "+data.response);
                        }
                    },
                    error : function(xhr, textStatus,
                                     errorThrown) {
                        alert("The server is busy. Please try again later")
                        console
                            .error("The server is busy. Please try again later");
                        popup.close();
                    },
                    complete : function() {
                        document
                            .getElementById("loadingSpinner").style.display = "none";

                    }
                });

        });