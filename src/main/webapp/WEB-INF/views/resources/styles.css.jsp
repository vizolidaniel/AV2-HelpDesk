<%@ page language="java" contentType="text/css; charset=UTF-8" pageEncoding="UTF-8"%>
select {
    font: inherit;
}

table {
    width: 100%;
    margin-bottom: 1rem;
}

table th,
table td {
    padding: 0.5rem;
    text-align: center;
    border: solid 0.05rem #cecece;
    border-top: none;
}

table th:first-child,
table td:first-child {
    border-left: none;
}

table th:last-child,
table td:last-child {
    border-right: none;
}

.page-controls {
    margin: auto;
    width: 8rem;
    display: flex;
    justify-content: space-between;
}

fieldset {
    border: none;
    display: flex;
    flex-direction: column;
}
textarea {
    min-width: 100%;
    width: 100%;
    max-width: 50rem;
    max-height: 20.5rem;
    min-height: 20.5rem;
}