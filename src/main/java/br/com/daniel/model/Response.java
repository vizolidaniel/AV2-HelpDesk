package br.com.daniel.model;

import br.com.daniel.exception.PageableException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collector;

public class Response<T> {
    private T result;
    private Collection<T> results;
    private Long total;
    private Integer page;
    private Integer size;

    public Builder<T> builder() {
        return new Builder<>(this);
    }

    public T getResult() {
        return result;
    }

    public Collection<T> getResults() {
        return results;
    }

    public Long getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    @JsonProperty("hasNext")
    public Boolean hasNext() {
        if (this.results == null || this.page == null) return null;

        return this.page < this.getTotalPages();
    }

    @JsonProperty("hasPrevious")
    public Boolean hasPrevious() {
        if (this.results == null || this.page == null) return null;

        return this.page > 1;
    }

    public <S> Response<S> map(final Function<T, S> mapping) {
        return new Response<S>().builder().result(mapping.apply(this.result)).build();
    }

    public <S, R extends Collection<S>> Response<S> map(final Function<T, S> mapping, Collector<S, ?, R> collector) {
        final Collection<S> mappedResults = this.results.stream().map(mapping).collect(collector);
        return new Response<S>()
                .builder()
                .results(mappedResults)
                .page(this.page)
                .size(this.size)
                .total(this.total)
                .build();
    }

    @JsonIgnore
    private long getTotalPages() {
        if (this.total == null || this.total == 0) return 0;
        if (this.size == null || this.size == 0) return 0;

        long pages = this.total / this.size;
        int remainingPage = this.total % this.size > 0 ? 1 : 0;

        return pages + remainingPage;
    }

    public static class Builder<T> {
        private final Response<T> response;

        private Builder(final Response<T> response) {
            this.response = response;
        }

        public Result<T> result(T result) {
            this.response.result = result;
            return new Result<>(this.response);
        }

        public Results<T> results(Collection<T> results) {
            this.response.results = results;
            this.response.size = results.size();
            this.response.total = (long) results.size();
            return new Results<>(this.response);
        }

        public static class Result<T> {
            private final Response<T> response;

            public Result(final Response<T> response) {
                this.response = response;
            }

            public Response<T> build() {
                return this.response;
            }
        }

        public static class Results<T> {
            private final Response<T> response;

            public Results(final Response<T> response) {
                this.response = response;
            }

            public Response<T> build() {
                return this.response;
            }

            public Results<T> page(final int page) {
                if (page < 1) throw new PageableException("page must be greater then 0");

                this.response.page = page;
                return this;
            }

            public Results<T> size(final int size) {
                if (size < 1) throw new PageableException("size must be greater then 0");

                this.response.size = size;
                return this;
            }

            public Results<T> total(final long total) {
                if (total < 0) throw new PageableException("total must be a positive number");

                this.response.total = total;
                return this;
            }
        }
    }
}
