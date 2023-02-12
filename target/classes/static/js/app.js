function startTime() {
  const today = new Date();
  let yyyy = today.getFullYear();
  let mm = today.getMonth()+1;
  let dd = today.getDate();
  let hh = today.getHours();
  let min = today.getMinutes();
  let ss = today.getSeconds();
  mm = checkTime(mm);
  dd = checkTime(dd);
  min = checkTime(min);
  ss = checkTime(ss);
  document.getElementById('currentDateTime').innerHTML =  yyyy + "-" + mm + "-" + dd + " " + hh + ":" + min + ":" + ss;
  setTimeout(startTime, 1000);
}

function checkTime(i) {
  if (i < 10) {i = "0" + i};  // add zero in front of numbers < 10
  return i;
}

function validateSearchStructures() {
    var city = document.getElementById("city").value;
    if (city == '') {
        alert('Please enter the city');
        return false;
    } else {
        return true;
    }
}

function validateLogin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    if (username == '' || password == '') {
        alert('Please enter both username and password');
        return false;
    } else {
        return true;
    }
}

function validateSignin() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var firstname = document.getElementById("firstname").value;
    var lastname = document.getElementById("lastname").value;
    var email = document.getElementById("email").value;
    var phonenumber = document.getElementById("phonenumber").value;
    var city = document.getElementById("city").value;
    var address = document.getElementById("address").value;
    var zipcode = document.getElementById("zipcode").value;
    if (username == '' || password == '' || firstname == ''
         || lastname == '' || email == '' || phonenumber == ''
          || city == '' || address == '' || zipcode == ''
    ) {
        alert('Please enter all data required');
        return false;
    } else {
        return true;
    }
}

function validateUpdateProfile() {
    var firstname = document.getElementById("firstname").value;
    var lastname = document.getElementById("lastname").value;
    var email = document.getElementById("email").value;
    var phonenumber = document.getElementById("phonenumber").value;
    var city = document.getElementById("city").value;
    var address = document.getElementById("address").value;
    var zipcode = document.getElementById("zipcode").value;
    if (firstname == '' || lastname == '' || email == '' || phonenumber == ''
          || city == '' || address == '' || zipcode == ''
    ) {
        alert('Please enter all data required');
        return false;
    } else {
        return true;
    }
}

function validateChangePassword() {
    var oldPassword = document.getElementById("oldPassword").value;
    var newPassword = document.getElementById("newPassword").value;
    if (oldPassword == '' || newPassword == '') {
        alert('Please enter both old and new password');
        return false;
    } else {
        return true;
    }
}