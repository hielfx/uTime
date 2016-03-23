'use strict';

angular.module('volunteercrowdApp')
    .controller('RequestController', function ($scope, $state, Request, RequestSearch, ParseLinks) {

        $scope.requests = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Request.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.requests = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            RequestSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.requests = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.request = {
                creationDate: null,
                description: null,
                code: null,
                finishDate: null,
                deleted: null,
                id: null
            };
        };
    });
