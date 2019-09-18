/**
 * Title : Assignment 01
 * Student Id : A01045229 
 * Name : Jessica Kim
 * Date : 2019-09-09
 **/

jQuery(document).ready(function ($) {
    let imgList = $("body").attr("data-imgList");
    let dateList = $("body").attr("data-dateList");
    let captionList = $("body").attr("data-captionList");
    let count = 2;
    let autoModeOn = false;
    let img;
    let timer;
    let imageArr;

    if (!!imgList && !!dateList && !!captionList) {
        imageArr = stringToArrayFormatter(imgList, dateList, captionList);
        console.log("imageArr: ", imageArr);
    }

    $('#logout').click(() => {
        $.post("assign01", {
            value: "invalidate"
        }, (data, status) => {
            console.log("status: " + status + " /Successfully invalidated")
            alert(status);
            window.location.replace("http://localhost:8081/assignment1/login.html")
        });
    });

    $('#next').click(function () {
        nextImage();
    });
    $('#delete').click(function () {
        deleteImage(); //ajax call is super cool 
        let filename = $("#caption").text(); //'data' is callback 
        $.post("assign01-2", {
            value: "delete",
            filename: filename
        }, function (data) {

            $.get("assign01", {}, function () {
                document.location.reload();
            });
        });
        // return false; to refresh 
    });

    $('#prev').click(function () {

        prevImage();

    });
    $('#auto').click(function () {

        autoImage();

    });
    $('#mainPage').click(function () {
        window.location.replace("http://localhost:8081/assignment1/login.html");
    });

    function stringToArrayFormatter(imgArr, dateArr, captionArr) {
        let transformedImgArr = imgArr.slice(1, -1).replace(/\s/g, '').split(',');
        let transformedDateArr = dateArr.slice(1, -1).replace(/\s/g, '').split(',');
        let transformedCaptionArr = captionArr.slice(1, -1).replace(/\s/g, '').split(',');

        return arrayToJSONFormatter(transformedImgArr, transformedDateArr, transformedCaptionArr);
    }

    function arrayToJSONFormatter(arr1, arr2, arr3) {
        let len = arr1.length;
        let localjsonobj;
        let parsedjson = [];
        let tostring;
        if (arr2.length === len && arr3.length === len) {
            for (let i = 0; i < len; i++) {
                localjsonobj = "{\"imageSrc\":\"../upload/images/" + arr1[i].toString() + "\",\"caption\": \"" + arr2[i].toString() + "\",\"date\":\"" + arr3[i].toString() + "\"}"

                console.log(localjsonobj);

                parsedjson.push(JSON.parse(localjsonobj));

            }
        } else {
            console.log("length of arrays are different");
        }
        return parsedjson;
    }

    function setImage() {
        if (!!img) {
            document.getElementById("caption").innerHTML = img.caption;
            document.getElementById("date").innerHTML = img.date;
            document.getElementById("changeImg").src = img.imageSrc;
        }
    }

    function nextImage() {
        console.log('count: ', count);
        ++count;
        img = imageArr[count % imageArr.length];
        setImage();

    }

    function deleteImage() {
        document.getElementById("changeImg").src = './images/default.jpg';
    }

    function prevImage() {
        --count;
        if (count < 0) {
            count = 2;
        }
        // console.log('count: ', count);
        img = imageArr[count % imageArr.length];
        setImage();
    }

    function autoImage() {
        if (!autoModeOn) {
            timer = setInterval(() => {
                console.log("auto mode");
                img = imageArr[++count % imageArr.length];
                setImage();
            }, 2000);
            autoModeOn = true;
        } else if (!!timer) {
            console.log("canceling auto mode....");
            clearInterval(timer);
            autoModeOn = false;
        }
    }
});