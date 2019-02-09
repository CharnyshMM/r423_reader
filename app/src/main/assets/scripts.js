
    let globalContents = {};
    function getChapters() {
        let resultArr = [];
        let idsArr = [];

        let hArr = getChaptersTags();
        if (hArr === null || hArr.length === 0) return ;

        let retText = "";
        let size = hArr.length;
        for (let i=0; i<size; i++) {
            if (!isNaN(+hArr[i].innerText.substr(0,1))) {
                if (retText !== "") {
                    resultArr.push(retText);
                    retText = "";
                }
                let id = generateId();
                hArr[i].setAttribute("id", id);
                hArr[i].style.backgroundColor = "red";
                idsArr.push(id);
                retText += hArr[i].innerText;
            } else {
                retText += " " + hArr[i].innerText;
            }
            if (i === size-1) {
                resultArr.push(retText);
            }
        }
        console.log("SCRIPT IS RUNNING!!!!");

        return addToJson(idsArr, resultArr);
    }

    function addToJson(ids, texts) {
        if (ids===null || texts===null || ids.length!==texts.length) {
            throw new DOMException("ids size != texts size");
        }
        let objects = [];
        for (let i=0; i<ids.length; i++) {
            objects.push({id: ids[i], text: texts[i]});
        }
        return objects;
    }

    function getChaptersTags() {
        return Array.prototype.slice.call(document.querySelectorAll("h1, h2, h3, .MsoTitle"));
    }

    function generateId () {
        return '_' + Math.random().toString(36).substr(2, 9);
    }

    function getContentsAndStoreGlobally() {
        globalContents = getChapters();
    }

    function getGlobalContents() {
        return globalContents;
    }

    window.addEventListener('load',getContentsAndStoreGlobally);

    function scrollToElement(id) {
        var elem = document.getElementById(id);
        console.log(elem);
        elem.scrollIntoView(true);
    }
