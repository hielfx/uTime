(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('curriculum', {
            parent: 'entity',
            url: '/curriculum?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.curriculum.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/curriculum/curricula.html',
                    controller: 'CurriculumController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('curriculum');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('curriculum-detail', {
            parent: 'entity',
            url: '/curriculum/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.curriculum.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/curriculum/curriculum-detail.html',
                    controller: 'CurriculumDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('curriculum');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Curriculum', function($stateParams, Curriculum) {
                    return Curriculum.get({id : $stateParams.id});
                }]
            }
        })
        .state('curriculum.new', {
            parent: 'curriculum',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/curriculum/curriculum-dialog.html',
                    controller: 'CurriculumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                website: null,
                                creationDate: null,
                                modificationDate: null,
                                file: null,
                                fileContentType: null,
                                statement: null,
                                vision: null,
                                mission: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('curriculum', null, { reload: true });
                }, function() {
                    $state.go('curriculum');
                });
            }]
        })
        .state('curriculum.edit', {
            parent: 'curriculum',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/curriculum/curriculum-dialog.html',
                    controller: 'CurriculumDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Curriculum', function(Curriculum) {
                            return Curriculum.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('curriculum', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('curriculum.delete', {
            parent: 'curriculum',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/curriculum/curriculum-delete-dialog.html',
                    controller: 'CurriculumDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Curriculum', function(Curriculum) {
                            return Curriculum.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('curriculum', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
